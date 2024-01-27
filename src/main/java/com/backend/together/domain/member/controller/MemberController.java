package com.backend.together.domain.member.controller;

import com.backend.together.domain.member.dto.MemberDto;
import com.backend.together.domain.member.dto.TokenDto;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.entity.enums.Gender;
import com.backend.together.domain.member.entity.enums.MemberStatus;
import com.backend.together.domain.member.entity.enums.Provider;
import com.backend.together.domain.member.service.MemberService;
import com.backend.together.domain.member.social.*;
import com.backend.together.global.response.ResponseDto;
import com.backend.together.global.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDto memberDto)
    {
        try{
            MemberEntity memberEntity = MemberEntity.builder()
                    .nickname(memberDto.getNickname())
                    .email(memberDto.getEmail())
                    .password(passwordEncoder.encode(memberDto.getPassword()))
                    .age(memberDto.getAge())
                    .gender(memberDto.getGender())
                    .station(memberDto.getStation())
                    .name(memberDto.getName())
                    .phoneNumber(memberDto.getPhoneNumber())
                    .memberStatus(MemberStatus.NORMAL)
                    .provider(Provider.LOCAL)
                    .build();
            memberService.create(memberEntity);

            ResponseDto responseDTO = ResponseDto.builder().isSuccess(true).code(200).message("회원가입 성공").build();
            return ResponseEntity.ok().body(responseDTO);
        }
        catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder().isSuccess(false).code(400).message("회원가입 실패 - "+e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody MemberDto memberDto)
    {
        MemberEntity memberEntity = memberService.getByCredentials(memberDto.getEmail(), memberDto.getPassword(),passwordEncoder);
        if(memberEntity != null){
            final String token = tokenProvider.create(memberEntity);
            final TokenDto tokenDto = TokenDto.builder()
                    .token(token).build();
            List<TokenDto> result = new ArrayList<>();
            result.add(tokenDto);

            ResponseDto responseDto=ResponseDto.<TokenDto>builder().code(200).isSuccess(true).message("로그인 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }
        else{
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).isSuccess(false).message("로그인 실패 - 이메일 또는 비밀번호를 잘못 입력했습니다.")
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 계정(이메일) 찾기 *** 수정하기(전화번호 인증?)
    @PostMapping("/findEmail")
    public ResponseEntity<ResponseDto> findEmailByNickname(@RequestBody MemberDto memberDto){
        try{
            String email = memberService.findEmailByNickname(memberDto.getNickname());
            List<String> result=new ArrayList<>();
            result.add(email);
            ResponseDto responseDto = ResponseDto.<String>builder().code(200).isSuccess(true).data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).isSuccess(false).message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }

    }

    // 비밀번호 찾기
    @PostMapping("/findPassword")
    public ResponseEntity<ResponseDto> findPasswordByEmail(@RequestBody MemberDto memberDto){
        try{
            boolean isSuccess = memberService.findPasswordByEmail(memberDto.getEmail());
            List<Boolean> result=new ArrayList<>();
            result.add(isSuccess);
            ResponseDto responseDto = ResponseDto.<Boolean>builder().code(200).isSuccess(true).message("비밀번호 찾기 성공 - 비밀번호를 변경해주세요.").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).isSuccess(false).message("비밀번호 찾기 실패 - "+e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 비밀번호 변경
    @PatchMapping("/changePassword")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody MemberDto memberDto){
        try{
            boolean isSuccess = memberService.changePassword(memberDto.getEmail(),passwordEncoder.encode(memberDto.getPassword()));
            List<Boolean> result = new ArrayList<>();
            result.add(isSuccess);
            ResponseDto responseDto = ResponseDto.<Boolean>builder().code(200).isSuccess(true).message("비밀번호 변경 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).isSuccess(false).message("비밀번호 변경 실패 - "+e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    //구글로 로그인
    @GetMapping ("/google")
    public ResponseEntity<ResponseDto> googleLogin(@RequestParam String code){
        GoogleToken googleToken = memberService.getGoogleToken(code);
        GoogleProfile googleProfile = memberService.getGoogleProfile(googleToken);
        try {
            MemberEntity memberEntity = MemberEntity.builder()
                    .nickname(googleProfile.name)  //이름은 구글에서 제공하는 닉네임
                    .password(UUID.randomUUID().toString())  //비밀번호는 랜덤값으로 지정
                    .email(googleProfile.email) //이메일 : 구글에서 제공하는 이메일
                    .memberStatus(MemberStatus.NORMAL)
                    .provider(Provider.GOOGLE)
                    .build();
            //memberService.create(memberEntity);

            String token = memberService.googleLogin(memberEntity);
            TokenDto tokenDto = TokenDto.builder()
                    .token(token).build();
            List<TokenDto> result = new ArrayList<>();
            result.add(tokenDto);

            ResponseDto responseDto = ResponseDto.<TokenDto>builder()
                    .code(200).isSuccess(true).message("구글 로그인 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }
        catch (Exception e){
        ResponseDto responseDto = ResponseDto.builder()
                .code(400).message("구글 로그인 실패 - " + e.getMessage()).isSuccess(false).build();
        return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 카카오로 로그인
    @GetMapping ("/kakao")
    public ResponseEntity<ResponseDto> kakaoLogin(@RequestParam String code){
        KakaoToken kakaoToken = memberService.getKakaoToken(code);
        KakaoProfile kakaoProfile = memberService.getKakaoProfile(kakaoToken);

        try {// 카카오 : 프로필 사진이랑 닉네임만 제공
            MemberEntity memberEntity = MemberEntity.builder()
                    .nickname(kakaoProfile.getProperties().getNickname())  //이름은 카카오에서 제공하는 닉네임
                    .email(kakaoProfile.getId().toString()) //이메일: 카카오제공하는 id로(카카오에서 제공X)
                    .password(UUID.randomUUID().toString())  //비밀번호: 랜덤값으로 지정
                    .image(kakaoProfile.getProperties().getProfile_image())
                    .memberStatus(MemberStatus.NORMAL)
                    .provider(Provider.KAKAO)
                    .build();

            String token = memberService.kakaoLogin(memberEntity);

            TokenDto tokenDto = TokenDto.builder()
                    .token(token).build();
            List<TokenDto> result = new ArrayList<>();
            result.add(tokenDto);

            ResponseDto responseDto = ResponseDto.<TokenDto>builder()
                    .code(200).isSuccess(true).message("카카오 로그인 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }
        catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).message("카카오 로그인 실패 - " + e.getMessage()).isSuccess(false).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 네이버로 로그인
    @GetMapping ("/naver")
    public ResponseEntity<ResponseDto> naverLogin(@RequestParam String code) {
        NaverToken naverToken = memberService.getNaverToken(code);
        NaverProfile naverProfile = memberService.getNaverProfile(naverToken);

        try {
            MemberEntity memberEntity = MemberEntity.builder()
                    .nickname(naverProfile.getResponse().getNickname())
                    .email(naverProfile.getResponse().getEmail())
                    .password(UUID.randomUUID().toString())  //비밀번호: 랜덤값으로 지정
                    .image(naverProfile.getResponse().getProfile_image())
                    .age(naverProfile.getResponse().getAge())
                    .gender(naverProfile.getResponse().getGender())
                    .memberStatus(MemberStatus.NORMAL)
                    .provider(Provider.NAVER)
                    .build();

            String token = memberService.naverLogin(memberEntity);

            TokenDto tokenDto = TokenDto.builder()
                    .token(token).build();
            List<TokenDto> result = new ArrayList<>();
            result.add(tokenDto);

            ResponseDto responseDto = ResponseDto.<TokenDto>builder()
                    .code(200).isSuccess(true).message("네이버 로그인 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).message("네이버 로그인 실패 - " + e.getMessage()).isSuccess(false).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 인증번호 확인하기
    @GetMapping("/checkSms")
    public ResponseEntity<ResponseDto> sendSMS(@RequestParam String phoneNumber) {

        try {
            Random rand = new Random(); // 인증번호 : 랜덤값 4자리
            String numStr = "";
            for (int i = 0; i < 4; i++) {
                String ran = Integer.toString(rand.nextInt(10));
                numStr += ran;
            }

            System.out.println("수신자 번호 : " + phoneNumber);
            System.out.println("인증번호 : " + numStr);
            memberService.sendOne(phoneNumber, numStr);

            List<String> result = new ArrayList<>();
            result.add(numStr);

            ResponseDto responseDto = ResponseDto.<String>builder()
                    .code(200).isSuccess(true).message("실명인증 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).message("실명인증 실패 - " + e.getMessage()).isSuccess(false).build();
            return ResponseEntity.badRequest().body(responseDto);
        }

    }

}

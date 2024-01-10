package com.backend.together.domain.member.controller;

import com.backend.together.domain.member.dto.MemberDto;
import com.backend.together.domain.member.dto.TokenDto;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.service.MemberService;
import com.backend.together.global.response.ResponseDto;
import com.backend.together.global.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
                    .image(".") // 나중에 수정하기
                    .age(20)
                    .build();
            memberService.create(memberEntity);

            ResponseDto responseDTO = ResponseDto.builder().status(200).Message("회원가입 성공").success(true).build();
            return ResponseEntity.ok().body(responseDTO);
        }
        catch (Exception e){
            ResponseDto responseDTO= ResponseDto.builder().status(400).Message("회원가입 실패 - "+e.getMessage()).success(false).build();
            return ResponseEntity.badRequest().body(responseDTO);
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

            ResponseDto responseDto=ResponseDto.<TokenDto>builder().status(200).success(true).Message("로그인 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }
        else{
            ResponseDto responseDto = ResponseDto.builder()
                    .status(400).success(false).Message("로그인 실패")
                    .build();
            return ResponseEntity.ok().body(responseDto);
        }
    }

    // 계정(이메일) 찾기
    @PostMapping("/findEmail")
    public ResponseEntity<ResponseDto> findEmailByNickname(@RequestBody MemberDto memberDto){
        try{
            String email = memberService.findEmailByNickname(memberDto.getNickname());
            List<String> result=new ArrayList<>();
            result.add(email);
            ResponseDto responseDto = ResponseDto.<String>builder().status(200).success(true).data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .status(400).success(false).Message(e.getMessage())
                    .build();
            return ResponseEntity.ok().body(responseDto);
        }

    }

    // 비밀번호 찾기
    @PostMapping("/findPassword")
    public ResponseEntity<ResponseDto> findPasswordByEmail(@RequestBody MemberDto memberDto){
        try{
            boolean isSuccess = memberService.findPasswordByEmailAndNickname(memberDto.getEmail(),memberDto.getNickname());
            List<Boolean> result=new ArrayList<>();
            result.add(isSuccess);
            ResponseDto responseDto = ResponseDto.<Boolean>builder().status(200).success(true).data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .status(400).success(false).Message(e.getMessage())
                    .build();
            return ResponseEntity.ok().body(responseDto);
        }
    }

    // 비밀번호 변경
    @PatchMapping("/changePassword")
    public ResponseEntity<ResponseDto>  changePassword(@RequestBody MemberDto memberDto){
        try{
            boolean isSuccess = memberService.changePassword(memberDto.getNickname(),memberDto.getEmail(),passwordEncoder.encode(memberDto.getPassword()));
            List<Boolean> result = new ArrayList<>();
            result.add(isSuccess);
            ResponseDto responseDto = ResponseDto.<Boolean>builder().status(200).success(true).data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDTO = ResponseDto.builder()
                    .status(400).success(false).Message(e.getMessage())
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }
    }

}

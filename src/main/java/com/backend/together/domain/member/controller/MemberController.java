package com.backend.together.domain.member.controller;

import com.backend.together.domain.member.dto.MemberDto;
import com.backend.together.domain.member.dto.TokenDto;
import com.backend.together.domain.member.entity.Member;
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
            Member member = Member.builder()
                    .nickname(memberDto.getNickname())
                    .email(memberDto.getEmail())
                    .password(passwordEncoder.encode(memberDto.getPassword()))
                    .image(".") // 나중에 수정하기
                    .age(20)
                    .build();
            memberService.create(member);

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
        Member member = memberService.getByCredentials(memberDto.getEmail(), memberDto.getPassword(),passwordEncoder);
        if(member != null){
            final String token = tokenProvider.create(member);
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

}

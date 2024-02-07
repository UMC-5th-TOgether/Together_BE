package com.backend.together.domain.mypage.controller;

import com.backend.together.domain.member.dto.MemberDto;
import com.backend.together.domain.member.service.MemberService;
import com.backend.together.domain.mypage.dto.MyInfoDto;
import com.backend.together.domain.mypage.service.MyPageService;
import com.backend.together.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/myPage")
public class MyPageController {
    @Autowired
    MyPageService myPageService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 내 정보 조회하기
    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal String memberId){
        try {
            MyInfoDto result = myPageService.getMyInfo(memberId);
            ResponseDto responseDto = ResponseDto.builder().code(200).isSuccess(true).message("내 정보 조회 성공").data(Collections.singletonList(result)).build();
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).isSuccess(false).message("내 정보 조회 실패 - "+e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }

    }

    // 비밀번호 확인하기
    @PostMapping("/checkPassword")
    public ResponseEntity<ResponseDto> checkPassword(@RequestBody MemberDto memberDto, @AuthenticationPrincipal String memberId) {
        try {
            myPageService.checkPassword(memberId, memberDto.getPassword(),passwordEncoder);
            ResponseDto responseDto = ResponseDto.builder().code(200).message("비밀번호 확인 성공").isSuccess(true).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder().code(400).message("비밀번호 확인 실패 - "+e.getMessage()).isSuccess(false).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 회원탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteMember(@AuthenticationPrincipal String memberId){
        try{
            myPageService.deleteMember(memberId);
            ResponseDto responseDto = ResponseDto.builder().code(200).message("회원탈퇴 성공").isSuccess(true).build();
            return ResponseEntity.ok().body(responseDto);
        }
        catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder().code(400).message("회원탈퇴 실패 - "+e.getMessage()).isSuccess(false).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 닉네임 변경
    @PatchMapping("/changeNickname")
    public ResponseEntity<ResponseDto> changeNickname(@RequestBody MemberDto memberDto, @AuthenticationPrincipal String memberId){
        try{
            boolean isSuccess = myPageService.changeNickname(memberId, memberDto.getNickname());
            List<Boolean> result = new ArrayList<>();
            result.add(isSuccess);
            ResponseDto responseDto = ResponseDto.<Boolean>builder().code(200).isSuccess(true).message("닉네임 변경 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).isSuccess(false).message("닉네임 변경 실패 - "+e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 프로필 이미지 변경
    @PostMapping("/changeImage")
    public ResponseEntity<ResponseDto> changeImage(@AuthenticationPrincipal String memberId, @RequestPart("file") MultipartFile image){
        try{
            boolean isSuccess = myPageService.changeImage(memberId, image);
            List<Boolean> result = new ArrayList<>();
            result.add(isSuccess);
            ResponseDto responseDto = ResponseDto.<Boolean>builder().code(200).isSuccess(true).message("이미지 업로드 성공").data(result).build();
            return ResponseEntity.ok().body(responseDto);

        }catch (Exception e){
            ResponseDto responseDto = ResponseDto.builder()
                    .code(400).isSuccess(false).message("이미지 업로드 실패 - "+e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

}

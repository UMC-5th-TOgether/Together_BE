package com.backend.together.domain.mypage.controller;

import com.backend.together.domain.mypage.dto.MyInfoDto;
import com.backend.together.domain.mypage.service.MyPageService;
import com.backend.together.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/myPage")
public class MyPageController {
    @Autowired
    MyPageService myPageService;

    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal String memberId){
        MyInfoDto result = myPageService.getMyInfo(Long.parseLong(memberId));
        ResponseDto responseDto = ResponseDto.builder().status(200).success(true).data(Collections.singletonList(result)).build();
        return ResponseEntity.ok().body(responseDto);
    }

}

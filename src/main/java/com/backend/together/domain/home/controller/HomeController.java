package com.backend.together.domain.home.controller;

import com.backend.together.domain.home.dto.HomeResponseDTO;
import com.backend.together.domain.home.service.HomeServiceImpl;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.post.Post;
import com.backend.together.global.apiPayload.ApiResponse;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {
    private final HomeServiceImpl homeService;
    private final MemberRepository memberRepository;
    @GetMapping()
    public ApiResponse<HomeResponseDTO.GetTop8PostListDTO> getPostListForHome(@RequestParam String category){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<Post> postList = homeService.getPostListForHome(category, userId);

        return ApiResponse.onSuccess(HomeResponseDTO.GetTop8PostListDTO.getTop8PostListDTO(postList, member));
    }
}

package com.backend.together.domain.home.controller;

import com.backend.together.domain.home.dto.HomeResponseDTO;
import com.backend.together.domain.home.service.HomeServiceImpl;
import com.backend.together.domain.post.Post;
import com.backend.together.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guest")
public class GuestHomeController {
    private final HomeServiceImpl homeService;
    @GetMapping()
    public ApiResponse<HomeResponseDTO.GetTop8PostListForGuestDTO> getPostListForHome(@RequestParam String category){
        List<Post> postList = homeService.getPostListForGuestHome(category);

        return ApiResponse.onSuccess(HomeResponseDTO.GetTop8PostListForGuestDTO.getTop8PostListForGuestDTO(postList));
    }
}

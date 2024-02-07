package com.backend.together.domain.review.controller;

import com.amazonaws.util.CollectionUtils;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.review.dto.ReviewRequestDTO;
import com.backend.together.domain.review.dto.ReviewResponseDTO;
import com.backend.together.domain.review.entity.Review;
import com.backend.together.domain.review.service.ReviewImageServiceImpl;
import com.backend.together.domain.review.service.ReviewServiceImpl;
import com.backend.together.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewServiceImpl reviewService;
    private final ReviewImageServiceImpl reviewImageService;
    @PostMapping() //리뷰 작성
    public ApiResponse<?> postReview(@RequestPart @Valid ReviewRequestDTO.PostReviewDTO request, @RequestPart(required = false) List<MultipartFile> requestImages) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Review newReview = reviewService.postReview(request, userId);

        if (!CollectionUtils.isNullOrEmpty(requestImages)) {
            requestImages.forEach(image -> reviewImageService.postReviewImage(image, newReview));
        }
        return ApiResponse.successWithoutResult();
    }

    @GetMapping("/write") //리뷰 작성하는 화면에서 멤버 정보를 불러오는 api
    public ApiResponse<?> getMemberInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        MemberEntity member = reviewService.getMemberInfo(userId);
        return ApiResponse.onSuccess(ReviewResponseDTO.GetUserInfoDTO.getUserInfo(member));
    }

    @GetMapping() //내가 쓴 리뷰
    public ApiResponse<?> getReviewByMe(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<Review> reviewList = reviewService.getReviewListByMe(userId);
        return ApiResponse.onSuccess(ReviewResponseDTO.GetReviewListByMeDTO.getReviewList(reviewList));
    }

    @GetMapping("/{reviewId}") //특정 리뷰 조회
    public ApiResponse<?> getReview(@PathVariable(name = "reviewId") Long reviewId) {
        Review reviewDetail = reviewService.getReviewDetail(reviewId);
        List<String> reviewImages = reviewImageService.getReviewImages(reviewId);

        return ApiResponse.onSuccess(ReviewResponseDTO.GetReviewDetailDTO.getReviewDetail(reviewDetail, reviewImages));
    }
}

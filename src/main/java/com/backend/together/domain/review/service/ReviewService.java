package com.backend.together.domain.review.service;

import com.backend.together.domain.review.dto.ReviewRequestDTO;
import com.backend.together.domain.review.dto.ReviewResponseDTO;
import com.backend.together.domain.review.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    Review postReview(ReviewRequestDTO.PostReviewDTO request, Long userId);
    List<Review> getReviewListByMe(Long userId);
    Review getReviewDetail(Long reviewId);
    ReviewResponseDTO.AggregationDTO getReviewAggregation(Long memberId);
    Page<Review> getReviewByMemberId(Long memberId, Integer page);
}

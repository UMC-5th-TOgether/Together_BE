package com.backend.together.domain.review.service;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.review.dto.ReviewRequestDTO;
import com.backend.together.domain.review.entity.Review;

import java.util.List;

public interface ReviewService {
    void postReview(ReviewRequestDTO.PostReviewDTO request, Long userId);
    MemberEntity getMemberInfo(Long userId);
    List<Review> getReviewListByMe(Long userId);
    Review getReviewDetail(Long reviewId);
}

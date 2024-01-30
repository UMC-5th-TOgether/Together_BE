package com.backend.together.domain.review.service;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.review.dto.ReviewRequestDTO;
import com.backend.together.domain.review.entity.Review;
import com.backend.together.domain.review.repository.ReviewRepository;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import com.backend.together.global.enums.ReviewEmotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    @Override
    public void postReview(ReviewRequestDTO.PostReviewDTO request, Long userId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        ReviewEmotion emotion = switch (request.getEmotion()) {
            case 0 -> ReviewEmotion.YES;
            case 1 -> ReviewEmotion.NO;
            default -> null;
        };

        Review newReview = request.toEntity(member, emotion);
        reviewRepository.save(newReview);
    }

    @Override
    public MemberEntity getMemberInfo(Long userId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return member;
    }

    @Override
    public List<Review> getReviewListByMe(Long userId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<Review> reviewList = reviewRepository.findAllByWriter(member);

        return reviewList;
    }

    @Override
    public Review getReviewDetail(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.REVIEW_NOT_FOUND));

        return review;
    }


}

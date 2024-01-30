package com.backend.together.domain.review.dto;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetUserInfoDTO {
        private Long userId;
        private String nickname;
        //private String gender;
        private Integer age;
        public static GetUserInfoDTO getUserInfo(MemberEntity member) {
            return GetUserInfoDTO.builder()
                    .userId(member.getMemberId())
                    .nickname(member.getNickname())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewListByMeDTO{
        List<GetReviewByMeDTO> reviewList;
        Integer listSize;

        public static GetReviewListByMeDTO getReviewList(List<Review> reviewList) {
            List<GetReviewByMeDTO> getReviewByMeList = reviewList.stream()
                    .map(GetReviewByMeDTO::getReviewByMe).toList();

            return GetReviewListByMeDTO.builder()
                    .reviewList(getReviewByMeList)
                    .listSize(getReviewByMeList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewByMeDTO {
        private Long reviewId;
        private String title;

        public static GetReviewByMeDTO getReviewByMe(Review review){
            return GetReviewByMeDTO.builder()
                    .reviewId(review.getId())
                    .title(review.getTitle())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewDetailDTO{
        private ReviewInfoDTO reviewInfo;
        private WriterInfoDTO writerInfo;

        public static GetReviewDetailDTO getReviewDetail(Review review) {
            ReviewInfoDTO reviewInfoDTO = ReviewInfoDTO.reviewInfo(review);
            WriterInfoDTO writerInfoDTO = WriterInfoDTO.writerInfo(review);

            return GetReviewDetailDTO.builder()
                    .reviewInfo(reviewInfoDTO)
                    .writerInfo(writerInfoDTO)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewInfoDTO{
        private Long reviewId;
        private String title;
        private String content;
        private String emotion;
        private Integer score;
        private String image;

        public static ReviewInfoDTO reviewInfo(Review review) {
            return ReviewInfoDTO.builder()
                    .reviewId(review.getId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .emotion(review.getEmotion().toString())
                    .score(review.getScore())
                    .image(review.getImage())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WriterInfoDTO {
        private Long memberId;
        private String nickname;
        //TODO: 나이, 성별 추가 필요

        public static WriterInfoDTO writerInfo(Review review) {
            return WriterInfoDTO.builder()
                    .memberId(review.getWriter().getMemberId())
                    .nickname(review.getWriter().getNickname())
                    .build();
        }
    }
}

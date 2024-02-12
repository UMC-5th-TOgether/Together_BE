package com.backend.together.domain.review.dto;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReviewResponseDTO {

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

        public static GetReviewDetailDTO getReviewDetail(Review review, List<String> reviewImages) {
            ReviewInfoDTO reviewInfoDTO = ReviewInfoDTO.reviewInfo(review, reviewImages);
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
        private List<String> imageUrls;

        public static ReviewInfoDTO reviewInfo(Review review, List<String> reviewImages) {
            return ReviewInfoDTO.builder()
                    .reviewId(review.getId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .emotion(review.getEmotion().toString())
                    .score(review.getScore())
                    .imageUrls(reviewImages)
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
                    .memberId(review.getReviewer().getMemberId())
                    .nickname(review.getReviewer().getNickname())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AggregationDTO {
        private MemberEntity friend;
        private Long reviewAll;
        private Long reviewEmotionYes;
        private Long avgScore;

        public static AggregationDTO aggregationDTO(MemberEntity member, Long reviewAll, Long reviewEmotionYes, Long avgScore){
            return AggregationDTO.builder()
                    .friend(member)
                    .reviewAll(reviewAll)
                    .reviewEmotionYes(reviewEmotionYes)
                    .avgScore(avgScore)
                    .build();
        }
    }
}

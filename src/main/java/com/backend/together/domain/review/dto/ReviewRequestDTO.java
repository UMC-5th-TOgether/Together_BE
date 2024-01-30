package com.backend.together.domain.review.dto;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.review.entity.Review;
import com.backend.together.global.enums.ReviewEmotion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewRequestDTO {

    //후기 작성
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostReviewDTO {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        private String image;

        @NotNull(message = "다시 만남 여부를 선택해주세요.")
        private Integer emotion;

        @Min(1)
        @Max(5)
        private Integer score;

        public Review toEntity(MemberEntity writer, ReviewEmotion emotion) {
            return Review.builder()
                    .writer(writer)
                    .title(title)
                    .content(content)
                    .image(image)
                    .emotion(emotion)
                    .score(score)
                    .build();
        }
    }
}

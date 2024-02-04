package com.backend.together.domain.matching.dto;

import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.matching.entity.MatchingImage;
import com.backend.together.domain.member.entity.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MatchingRequestDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    //1. 매칭 신청
    public static class PostMatchingDTO {
        @NotNull
        private Long receiverId; //매칭을 보낼 사용자의 아이디

        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        public Matching toEntity(MemberEntity receiver, MemberEntity sender) {
            return Matching.builder()
                    .receiver(receiver)
                    .sender(sender)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    //2. 매칭 수락 & 거절
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMatchingStatusDTO {
        @NotNull
        private Long matchingId; //상태를 변경할 매칭의 아이디
    }

    //3. 이미지 저장
    public static MatchingImage toMatchingImage(String imageUrl, Matching matching) {
        return MatchingImage.builder()
                .matching(matching)
                .imageUrl(imageUrl)
                .build();
    }
}

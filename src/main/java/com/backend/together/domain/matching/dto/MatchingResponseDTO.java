package com.backend.together.domain.matching.dto;

import com.backend.together.domain.matching.entity.Matching;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MatchingResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMatchingDetailDTO{
        private MatchingInfoDTO matching;
        private SenderInfoDTO writer;

        public static GetMatchingDetailDTO getMatchingDetail(Matching matching){
            MatchingInfoDTO matchingInfo = MatchingInfoDTO.matchingInfo(matching);
            SenderInfoDTO senderInfo = SenderInfoDTO.senderInfo(matching);

            return GetMatchingDetailDTO.builder()
                    .matching(matchingInfo)
                    .writer(senderInfo)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchingInfoDTO{
        Long matchingId;
        String matchingStatus;
        String title;
        String content;
        String profileImg;

        public static MatchingInfoDTO matchingInfo(Matching matching){
            return MatchingInfoDTO.builder()
                    .matchingId(matching.getId())
                    .matchingStatus(matching.getStatus().toString())
                    .title(matching.getTitle())
                    .content(matching.getContent())
                    .profileImg(matching.getImage())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderInfoDTO{
        Long userId;
        String nickname;
        //String gender;
        Integer age;

        public static SenderInfoDTO senderInfo(Matching matching){
            return SenderInfoDTO.builder()
                    .userId(matching.getSender().getMemberId())
                    .nickname(matching.getSender().getNickname())
                    .age(matching.getSender().getAge())
                    .build();
        }
    }
}

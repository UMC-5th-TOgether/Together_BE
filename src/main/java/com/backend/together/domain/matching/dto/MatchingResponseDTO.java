package com.backend.together.domain.matching.dto;

import com.backend.together.domain.matching.entity.Matching;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MatchingResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMatchingDetailDTO{
        private MatchingInfoDTO matching;
        private SenderInfoDTO writer;

        public static GetMatchingDetailDTO getMatchingDetail(Matching matching, List<String> matchingImages){
            MatchingInfoDTO matchingInfo = MatchingInfoDTO.matchingInfo(matching, matchingImages);
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
        List<String> images;

        public static MatchingInfoDTO matchingInfo(Matching matching, List<String> imageUrls){
            return MatchingInfoDTO.builder()
                    .matchingId(matching.getId())
                    .matchingStatus(matching.getStatus().toString())
                    .title(matching.getTitle())
                    .content(matching.getContent())
                    .images(imageUrls)
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
        String age;

        public static SenderInfoDTO senderInfo(Matching matching){
            return SenderInfoDTO.builder()
                    .userId(matching.getSender().getMemberId())
                    .nickname(matching.getSender().getNickname())
                    .age(matching.getSender().getAge())
                    .build();
        }
    }
}

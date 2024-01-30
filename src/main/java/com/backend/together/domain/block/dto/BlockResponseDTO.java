package com.backend.together.domain.block.dto;

import com.backend.together.domain.block.Entity.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BlockResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetBlockedListDTO {
        List<GetBlockedDTO> blockedList;
        Integer listSize;

        public static GetBlockedListDTO getBlockedList(List<Block> blockList) {
            List<GetBlockedDTO> getBlockedDTOList = blockList.stream()
                    .map(GetBlockedDTO::getBlocked).toList();

            return GetBlockedListDTO.builder()
                    .blockedList(getBlockedDTOList)
                    .listSize(getBlockedDTOList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetBlockedDTO {
        Long blockId;
        Long memberId;
        String nickname;

        public static GetBlockedDTO getBlocked(Block block) {
            return GetBlockedDTO.builder()
                    .blockId(block.getId())
                    .memberId(block.getBlocked().getMemberId())
                    .nickname(block.getBlocked().getNickname())
                    .build();
        }
    }
}

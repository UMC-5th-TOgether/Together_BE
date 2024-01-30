package com.backend.together.domain.block.dto;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.member.entity.MemberEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BlockRequestDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BlockMemberDTO {
        @NotNull
        private Long blockedId;

        public Block toEntity(MemberEntity blocker, MemberEntity blocked) {
            return Block.builder()
                    .blocker(blocker)
                    .blocked(blocked)
                    .build();
        }
    }
}

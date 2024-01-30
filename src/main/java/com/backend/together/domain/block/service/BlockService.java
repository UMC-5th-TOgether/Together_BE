package com.backend.together.domain.block.service;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.block.dto.BlockRequestDTO;

import java.util.List;

public interface BlockService {
    void createBlock(BlockRequestDTO.BlockMemberDTO request, Long userId);
    void deleteBlock(Long blockId);
    List<Block> getBlockedMember(Long userId);
}

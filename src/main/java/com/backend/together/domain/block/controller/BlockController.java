package com.backend.together.domain.block.controller;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.block.dto.BlockRequestDTO;
import com.backend.together.domain.block.dto.BlockResponseDTO;
import com.backend.together.domain.block.service.BlockServiceImpl;
import com.backend.together.global.apiPayload.ApiResponse;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/block")
public class BlockController {
    private final BlockServiceImpl blockService;

    @PostMapping()//차단
    public ApiResponse<?> createBlock(@RequestBody @Valid BlockRequestDTO.BlockMemberDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        if (userId.equals(request.getBlockedId())) {
            throw new CustomHandler(ErrorStatus.SELF_BLOCK_DECLINE);
        }

        blockService.createBlock(request, userId);
        return ApiResponse.successWithoutResult();
    }

    @GetMapping()//내가 차단한 목록 조회
    public ApiResponse<?> getBlockedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<Block> blockList = blockService.getBlockedMember(userId);
        return ApiResponse.onSuccess(BlockResponseDTO.GetBlockedListDTO.getBlockedList(blockList));
    }

    @DeleteMapping("/{blockId}") //차단 해제
    public ApiResponse<?> deleteBlock(@PathVariable(name = "blockId") Long blockId) {
        blockService.deleteBlock(blockId);
        return ApiResponse.successWithoutResult();
    }
}

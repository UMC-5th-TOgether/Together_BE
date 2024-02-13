package com.backend.together.domain.block.service;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.block.Repository.BlockRepository;
import com.backend.together.domain.block.dto.BlockRequestDTO;
import com.backend.together.domain.friend.entity.FriendList;
import com.backend.together.domain.friend.repository.FriendListRepository;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService{
    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final FriendListRepository friendListRepository;

    @Override
    public void createBlock(BlockRequestDTO.BlockMemberDTO request, Long userId) {
        MemberEntity blocker = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberEntity blocked = memberRepository.findByMemberId(request.getBlockedId())
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        FriendList friendList1 = friendListRepository.findByMember1AndMember2(blocker, blocked);
        FriendList friendList2 = friendListRepository.findByMember1AndMember2(blocked, blocker);

        //친구 목록에 있으면 삭제
        if (friendList1 != null) {
            friendListRepository.delete(friendList1);
        } else if (friendList2 != null) {
            friendListRepository.delete(friendList2);
        }

        Block newBlock = request.toEntity(blocker, blocked);
        blockRepository.save(newBlock);
    }

    @Override
    public void deleteBlock(Long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.BLOCK_NOT_FOUND));

        blockRepository.delete(block);
    }

    @Override
    public List<Block> getBlockedMember(Long userId) {
        MemberEntity blocker = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Block> blockList = blockRepository.findAllByBlocker(blocker);
        return blockList;
    }


}

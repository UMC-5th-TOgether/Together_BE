package com.backend.together.domain.friend.service;

import com.backend.together.global.enums.MatchingStatus;
import com.backend.together.domain.friend.entity.FriendList;
import com.backend.together.domain.friend.repository.FriendListRepository;
import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.matching.repository.MatchingRepository;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceImpl implements FriendService{
    private final MatchingRepository matchingRepository;
    private final FriendListRepository friendListRepository;
    private final MemberRepository memberRepository;
    @Override
    public List<Matching> getFollowingList(Long userId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return matchingRepository.findAllBySenderAndStatus(member, MatchingStatus.PENDING);
    }

    @Override
    public List<Matching> getFollowerList(Long userId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return matchingRepository.findAllByReceiverAndStatus(member, MatchingStatus.PENDING);
    }

    @Override
    public void addNewFriend(Matching matching) {
        //매칭 상태가 accept일 때만 친구 추가
        if (!matching.getStatus().equals(MatchingStatus.ACCEPT)){
            throw new CustomHandler(ErrorStatus.MATCHING_NOT_ACCEPT);
        }

        FriendList newFriendList = FriendList.builder()
                .member1(matching.getReceiver())
                .member2(matching.getSender())
                .build();

        friendListRepository.save(newFriendList);
    }

    @Override
    public List<FriendList> getRelationshipList(Long userId) {
        return friendListRepository.findByMemberId(userId);
    }
}

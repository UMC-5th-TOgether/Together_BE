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
import java.util.Objects;

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

    @Override
    public void deleteFriend(Long userId, Long memberId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberEntity friend = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        FriendList friendList1 = friendListRepository.findByMember1AndMember2(member, friend);
        FriendList friendList2 = friendListRepository.findByMember1AndMember2(friend, member);

        if (friendList1 == null && friendList2 == null) {
            throw new CustomHandler(ErrorStatus.FRIEND_NOT_EXIST); //친구 리스트에 없을 경우
        } else {
            friendListRepository.delete(Objects.requireNonNullElse(friendList1, friendList2)); //a가 null이면 b를 삭제한다
        }
    }
}

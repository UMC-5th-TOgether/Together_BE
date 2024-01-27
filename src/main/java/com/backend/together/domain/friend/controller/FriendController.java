package com.backend.together.domain.friend.controller;

import com.backend.together.domain.friend.dto.FriendResponseDTO;
import com.backend.together.domain.friend.entity.FriendList;
import com.backend.together.domain.friend.service.FriendServiceImpl;
import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendServiceImpl friendService;
    @GetMapping()
    public ApiResponse<FriendResponseDTO.GetFriendListDTO> getFriendList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        //1. 내 친구 관계 리스트를 가져옴(일단 member1 == 나이거나 member2 == 나일 때)
        List<FriendList> relationshipList = friendService.getRelationshipList(userId);

        //2. 내가 아닌 유저의 정보를 새로운 배열로 만듦
        List<MemberEntity> otherUserList = relationshipList.stream()
                .map(relationship -> {
                    if (relationship.getMember2().getMemberId().equals(userId)) {
                        return relationship.getMember1();
                    } else if (relationship.getMember1().getMemberId().equals(userId)) {
                        return relationship.getMember2();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return ApiResponse.onSuccess(FriendResponseDTO.GetFriendListDTO.getFriendListDTO(otherUserList));
    }
    @GetMapping("/following")
    public ApiResponse<FriendResponseDTO.GetFollowingListDTO> getFollowingList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<Matching> followingList = friendService.getFollowingList(userId);
        return ApiResponse.onSuccess(FriendResponseDTO.GetFollowingListDTO.getFollowingListDTO(followingList));
    }

    @GetMapping("/follower")
    public ApiResponse<FriendResponseDTO.GetFollowerListDTO> getFollowerList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<Matching> followerList = friendService.getFollowerList(userId);
        return ApiResponse.onSuccess(FriendResponseDTO.GetFollowerListDTO.getFollowerListDTO(followerList));
    }
}

package com.backend.together.domain.friend.dto;

import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.member.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FriendResponseDTO {
    //내가 매칭 신청 보낸 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowingListDTO{
        List<GetFollowingDTO> followingList;
        Integer listSize;

        public static GetFollowingListDTO getFollowingListDTO(List<Matching> followingList){
            List<GetFollowingDTO> getFollowingDTOList = followingList.stream()
                    .map(GetFollowingDTO::getFollowingDTO).toList();

            return GetFollowingListDTO.builder()
                    .followingList(getFollowingDTOList)
                    .listSize(getFollowingDTOList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowingDTO{
        Long matchingId;
        String profileImg;
        Long userId;
        String title;
        String status;

        public static GetFollowingDTO getFollowingDTO(Matching matching){
            return GetFollowingDTO.builder()
                    .matchingId(matching.getId())
                    .profileImg(matching.getImage())
                    .userId(matching.getReceiver().getMemberId())
                    .title(matching.getTitle())
                    .status(String.valueOf(matching.getStatus()))
                    .build();
        }
    }

    //내가 받은 매칭 신청 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowerListDTO{
        List<GetFollowerDTO> followerList;
        Integer listSize;

        public static GetFollowerListDTO getFollowerListDTO(List<Matching> followerList){
            List<GetFollowerDTO> getFollowerDTOList = followerList.stream()
                    .map(GetFollowerDTO::getFollowerDTO).toList();

            return GetFollowerListDTO.builder()
                    .followerList(getFollowerDTOList)
                    .listSize(getFollowerDTOList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowerDTO{
        Long matchingId;
        String profileImg;
        Long userId;
        String title;
        String status;

        public static GetFollowerDTO getFollowerDTO(Matching matching){
            return GetFollowerDTO.builder()
                    .matchingId(matching.getId())
                    .profileImg(matching.getImage())
                    .userId(matching.getReceiver().getMemberId())
                    .title(matching.getTitle())
                    .status(String.valueOf(matching.getStatus()))
                    .build();
        }
    }

    //친구 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFriendListDTO{
        List<GetFriendDTO> friendList;
        Integer listSize;

        public static GetFriendListDTO getFriendListDTO(List<MemberEntity> friendList){
            List<GetFriendDTO> getFriendDTOList = friendList.stream()
                    .map(GetFriendDTO::getFriendDTO).toList();

            return GetFriendListDTO.builder()
                    .friendList(getFriendDTOList)
                    .listSize(getFriendDTOList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFriendDTO{
        Long friendId;
        //String image;
        String nickname;
        //String profileMessage;

        public static GetFriendDTO getFriendDTO(MemberEntity member){
            return GetFriendDTO.builder()
                    .friendId(member.getMemberId())
                    .nickname(member.getNickname())
                    .build();
        }
    }
}

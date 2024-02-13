package com.backend.together.domain.home.dto;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HomeResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTop8PostListForGuestDTO {
        List<GetTop8PostDTO> postList;

        public static GetTop8PostListForGuestDTO getTop8PostListForGuestDTO(List<Post> postList){

            List<GetTop8PostDTO> getTop8PostDTOS = postList.stream().map(GetTop8PostDTO::getTop8PostDTO).toList();
            return GetTop8PostListForGuestDTO.builder()
                    .postList(getTop8PostDTOS)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTop8PostListDTO {
        String myNickname;
        List<GetTop8PostDTO> postList;

        public static GetTop8PostListDTO getTop8PostListDTO(List<Post> postList, MemberEntity member){

            List<GetTop8PostDTO> getTop8PostDTOS = postList.stream().map(GetTop8PostDTO::getTop8PostDTO).toList();
            return GetTop8PostListDTO.builder()
                    .myNickname(member.getNickname())
                    .postList(getTop8PostDTOS)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTop8PostDTO {
        Long postId;
        String title;
        String writerNickname;
        LocalDate accompaniedDate;
        Integer personNumMin;
        Integer personNumMax;
        String gender;
        List<String> hashtagList;

        public static GetTop8PostDTO getTop8PostDTO(Post post) {
            return GetTop8PostDTO.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .writerNickname(post.getMember().getNickname())
                    .accompaniedDate(post.getMeetTime())
                    .personNumMin(post.getPersonNumMin())
                    .personNumMax(post.getPersonNumMax())
                    .gender(post.getGender().toString())
                    .hashtagList(post.getPostHashtagList().stream().map(postHashtag -> postHashtag.getHashtag().getName()).collect(Collectors.toList()))
                    .build();
        }
    }
}

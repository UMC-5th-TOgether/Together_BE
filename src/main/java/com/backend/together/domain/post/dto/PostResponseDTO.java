package com.backend.together.domain.post.dto;

import com.backend.together.global.enums.Category;
import com.backend.together.domain.post.Post;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/*
* 24.2.5 생성일자 추가
*
*
* */
public class PostResponseDTO {

        Long id;
        @NotNull
        Long memberId;
        @NotNull
        String title;
        @NotNull
        Gender gender;
        @NotNull
        Integer personNumMin;
        Integer personNumMax;
        @NotNull
        String content;
        @NotNull
        PostStatus status;

        Long view;

        @NotNull // 바꿔야함
        Category category;

        @Builder.Default
        List<String> postHashtagList =  new ArrayList<>();

        // 24.2.5
// 24.2.5
        private LocalDate meetTime;

        public PostResponseDTO(Post post) {
                this.id = post.getId();
                this.memberId = post.getMember().getMemberId();
                this.title = post.getTitle();
                this.gender = post.getGender(); // enum
                this.personNumMin = post.getPersonNumMin();
                this.personNumMax = post.getPersonNumMax();
                this.content = post.getContent();
                this.status = post.getStatus(); // enum
                this.view = post.getView();

                // Load the category to avoid Hibernate proxy issues
                Hibernate.initialize(post.getCategory());
                this.category = post.getCategory();
                this.meetTime = post.getMeetTime();

                ////    this.postImageList = post.getPostImageList();
//        this.postImageList = postImageList != null ? new ArrayList<>(postImageList) : null;
            //        this.postHashtagList = post.getPostHashtagList();
//        this.postHashtagList = postHashtagList != null ? new ArrayList<>(postHashtagList) : null;
//                this.postHashtagList = getHashtag(post.getId());

        }
//
//        public List<String> getHashtag(Long postId) {
//                return postHashtagService.getHashtagToStringByPost(postId);
//        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class PagePostListDTO {
                Integer pageNo;
                Integer lastPage;
                boolean isLast;
                List<PagePostDTO> posts;

                public static PagePostListDTO pagePostListDTO (Page<Post> post, Integer requestPage) {
                        List<PagePostDTO> pagePostDTOS = post.stream().map(PagePostDTO::pagePostDTO).toList();
                        return PagePostListDTO.builder()
                                .posts(pagePostDTOS)
                                .pageNo(requestPage)
                                .lastPage(post.getTotalPages() - 1)
                                .isLast(requestPage.equals(post.getTotalPages() - 1))
                                .build();
                }
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class PagePostDTO {
                Long postId;
                String title;
                String writerNickname;
                LocalDate accompaniedDate;
                Integer personNumMin;
                Integer personNumMax;
                String gender;
                List<String> hashtagList;

                public static PagePostDTO pagePostDTO (Post post) {
                        return PagePostDTO.builder()
                                .postId(post.getId())
                                .title(post.getTitle())
                                .writerNickname(post.getMember().getNickname())
                                .accompaniedDate(post.getMeetTime())
                                .personNumMin(post.getPersonNumMin())
                                .personNumMax(post.getPersonNumMax())
                                .gender(post.getGender().toString())
                                .hashtagList(post.getPostHashtagList().stream().map(hasgtag -> hasgtag.getHashtag().getName()).collect(Collectors.toList()))
                                .build();
                }
        }
}

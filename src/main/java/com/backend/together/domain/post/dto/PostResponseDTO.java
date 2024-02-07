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
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
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

//        @Autowired
//        PostHashtagService postHashtagService;

        Long id;
        @NotNull
        Long memberId;
        @NotNull
        String title;
        @NotNull
        Gender gender;
        @NotNull
        Integer personNum;
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
                this.memberId = post.getMemberId();
                this.title = post.getTitle();
                this.gender = post.getGender(); // enum
                this.personNum = post.getPersonNum();
                this.content = post.getContent();
                this.status = post.getStatus(); // enum
                this.view = post.getView();

                // Load the category to avoid Hibernate proxy issues
                Hibernate.initialize(post.getCategory());
                this.category = post.getCategory();

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

}

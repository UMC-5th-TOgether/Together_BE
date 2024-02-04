package com.backend.together.domain.post.dto;

import com.backend.together.global.enums.Category;
import com.backend.together.domain.post.Post;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

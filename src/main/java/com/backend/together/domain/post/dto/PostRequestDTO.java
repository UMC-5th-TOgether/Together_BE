package com.backend.together.domain.post.dto;

import com.backend.together.domain.category.Category;
import com.backend.together.domain.enums.Gender;
import com.backend.together.domain.enums.PostStatus;
import com.backend.together.domain.mapping.PostHashtag;
import com.backend.together.domain.post.Hashtag;
//import com.backend.domain.post.PostImage;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.PostImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostRequestDTO {

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
//        @JsonIgnore // 무한 루프 방지
//        List<PostImage> postImageList;

        @NotNull // 바꿔야함
         Category category;
//    @JsonIgnore
//        List<PostHashtag> postHashtagList;


    public PostRequestDTO(Post post) {
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
        this.category = post.getCategory(); //enum
////    this.postHashtagList = post.getPostHashtagList();
//        this.postHashtagList = postHashtagList != null ? new ArrayList<>(postHashtagList) : null;

    }

    // member

    public static Post toEntity(final PostRequestDTO dto) {
        return Post.builder()

                .memberId(dto.getMemberId())
                .title(dto.getTitle())
                .gender(dto.getGender()) // enum
                .personNum(dto.getPersonNum())
                .content(dto.getContent())
                .status(dto.getStatus()) // enum
                .view(dto.getView())
//                .postImageList(dto.getPostImageList())
                .category(dto.getCategory()) // enum
//                .postHashtagList(dto.getPostHashtagList())
                .build();
    }

//    public Post toPost
}

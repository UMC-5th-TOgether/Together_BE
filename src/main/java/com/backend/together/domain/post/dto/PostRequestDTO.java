package com.backend.together.domain.post.dto;

import com.backend.together.domain.category.Category;
import com.backend.together.domain.enums.Gender;
import com.backend.together.domain.enums.PostStatus;
import com.backend.together.domain.post.Hashtag;
//import com.backend.domain.post.PostImage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PostRequestDTO {

    private static void PostCreateDTO () {
        @NotNull
         Long postId;

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

        @NotNull // 바꿔야함
         List<Category> categoryList = new ArrayList<>();

         // 바꿔야함
//         List<PostImage> postImageList = new ArrayList<>();

         // 바꿔야함
         List<Hashtag> postHashtagList = new ArrayList<>();

        // oneTomany member
    }
}

package com.backend.together.domain.post.dto;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.global.enums.Category;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
//import com.backend.domain.post.PostImage;
import com.backend.together.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostRequestDTO {

    @NotNull
    @Size(max = 40)
    String title;

    @NotNull
    Gender gender;

    @Min(1)
    Integer personNumMin;

    Integer personNumMax;

    @NotNull
    @Size(max = 255)
    String content;

    @NotNull
    LocalDate meetTime;

//        @JsonIgnore // 무한 루프 방지
//        List<PostImage> postImageList;

    @NotNull // 바꿔야함
    Category category;

    List<String> postHashtagList;


    public static Post toEntity(PostRequestDTO dto, MemberEntity member) {
        return Post.builder()
                .member(member)
                .title(dto.getTitle())
                .gender(dto.getGender()) // enum
                .personNumMin(dto.getPersonNumMin())
                .personNumMax(dto.getPersonNumMax())
                .content(dto.getContent())
                .meetTime(dto.getMeetTime())
                .status(PostStatus.ING)
                .view(0L)
//                .postImageList(dto.getPostImageList())
                .category(dto.getCategory()) // enum
//                .postHashtagList(dto.getPostHashtagList()) // 이렇게 받아도 외나?
                .build();
    }

//    public Post toPost
}
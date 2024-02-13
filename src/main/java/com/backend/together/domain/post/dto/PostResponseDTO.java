package com.backend.together.domain.post.dto;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.comment.dto.CommentMemberDTO;
import com.backend.together.domain.comment.dto.CommentResponseDTO;
import com.backend.together.domain.member.dto.MemberDto;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.converter.PostMemberConverter;
import com.backend.together.global.enums.Category;
import com.backend.together.domain.post.Post;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
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
//
//        PostMemberConverter postMemberConverter;
//        public PostResponseDTO(Post post, PostMemberConverter postMemberConverter) { // 변경된 부분
//                this.postMemberConverter = postMemberConverter; // 변경된 부분
//                this.id = post.getId();
////                this.memberDTO
//                this.memberDTO = new CommentMemberDTO(postMemberConverter.getMember(post.getMemberId()));
//                this.title = post.getTitle();
//                this.gender = post.getGender(); // enum
//                this.personNum = post.getPersonNum();
//                this.content = post.getContent();
//                this.status = post.getStatus(); // enum
//                this.view = post.getView();
//
//                // Load the category to avoid Hibernate proxy issues
//                Hibernate.initialize(post.getCategory());
//                this.category = post.getCategory();
//                this.createdAt = post.getCreatedAt();
//        }

        Long id;
//        @NotNull
//        Long memberId;
        CommentMemberDTO memberDTO;
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
        private LocalDateTime createdAt;

        public PostResponseDTO(Post post, CommentMemberDTO memberDTO) {
                this.id = post.getId();
//                this.memberDTO
                this.memberDTO = memberDTO;
                this.title = post.getTitle();
                this.gender = post.getGender(); // enum
                this.personNum = post.getPersonNum();
                this.content = post.getContent();
                this.status = post.getStatus(); // enum
                this.view = post.getView();

                // Load the category to avoid Hibernate proxy issues
                Hibernate.initialize(post.getCategory());
                this.category = post.getCategory();
                this.createdAt = post.getCreatedAt();
        }

        public PostResponseDTO(Post post) { // 이후 MemberEntity수정시 바꿔야함
                this.id = post.getId();
//                this.memberDTO
//                this.memberDTO = memberDTO;
                this.title = post.getTitle();
                this.gender = post.getGender(); // enum
                this.personNum = post.getPersonNum();
                this.content = post.getContent();
                this.status = post.getStatus(); // enum
                this.view = post.getView();

                // Load the category to avoid Hibernate proxy issues
                Hibernate.initialize(post.getCategory());
                this.category = post.getCategory();
                this.createdAt = post.getCreatedAt();
        }

        public static PostResponseDTO convertPostToDTO(Post post, MemberEntity member) {
        return
                new PostResponseDTO(post, new CommentMemberDTO(member)); // comment.getWriter()
}

}

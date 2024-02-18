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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import java.time.LocalDateTime;
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

        CommentMemberDTO memberDTO;
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
        private LocalDateTime createdAt;

        public PostResponseDTO(Post post, CommentMemberDTO memberDTO) {
                this.id = post.getId();

//                this.memberDTO
                this.memberDTO = memberDTO;

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
                this.createdAt = post.getCreatedAt();
                this.meetTime = post.getMeetTime();
        }

        public PostResponseDTO(Post post) { // 이후 MemberEntity수정시 바꿔야함
                this.id = post.getId();
//                this.memberDTO
                this.memberDTO = memberDTO;
                this.title = post.getTitle();
                this.gender = post.getGender(); // enum
//                this.personNum = post.getPersonNum();
                this.content = post.getContent();
                this.status = post.getStatus(); // enum
                this.view = post.getView();

                // Load the category to avoid Hibernate proxy issues
                Hibernate.initialize(post.getCategory());
                this.category = post.getCategory();
                this.createdAt = post.getCreatedAt();
        }

        public static PostResponseDTO convertPostToDTO(Post post, MemberEntity writer) {
        return
                new PostResponseDTO(post, new CommentMemberDTO(writer)); // comment.getWriter()

        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class PostResponseDTO2 {
                Long id;
                PostMemberDTO writer;
                String title;
                Gender gender;
                Integer personNumMin;
                Integer personNumMax;
                String content;
                PostStatus status;
                Long view;
                Category category;
                @Builder.Default
                List<String> postHashtagList =  new ArrayList<>();
                private LocalDate meetTime;
                private LocalDateTime createdAt;

                public static PostResponseDTO2 responseDTO2(Post post, MemberEntity writer, MemberEntity member) {
                        PostMemberDTO memberDTO = PostMemberDTO.postMemberDTO(member, writer);

                        return PostResponseDTO2.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .gender(post.getGender())
                                .personNumMax(post.getPersonNumMax())
                                .personNumMin(post.getPersonNumMin())
                                .content(post.getContent())
                                .status(post.getStatus())
                                .view(post.getView())
                                .category(post.getCategory())
                                .meetTime(post.getMeetTime())
                                .createdAt(post.getCreatedAt())
                                .writer(memberDTO)
                                .build();
                }
        }

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

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        public static class PostMemberDTO {

                private String nickname;
                private String age;
                private String gender;
                private String image;
                private boolean IsWriter;

                public static PostMemberDTO postMemberDTO(MemberEntity member, MemberEntity writer) {
                        return PostMemberDTO.builder()
                                .nickname(writer.getNickname())
                                .age(writer.getAge())
                                .gender(writer.getGender())
                                .image(writer.getImage())
                                .IsWriter(member.equals(writer))
                                .build();
                }

        }
}

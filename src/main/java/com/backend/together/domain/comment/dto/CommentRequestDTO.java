//package com.backend.together.domain.comment.dto;
//
//import com.backend.together.domain.comment.Comment;
//import com.backend.together.domain.post.Post;
//import com.backend.together.domain.post.dto.PostRequestDTO;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.Column;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.validation.constraints.NotNull;
//import lombok.*;
//import org.hibernate.Hibernate;
//import org.hibernate.annotations.ColumnDefault;
//@Data
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class CommentRequestDTO {
//
//    @NotNull
//    Long id;
//
//    @NotNull
//    Post postId;
//
//    @NotNull
//    Long memberId;
//
//    String content;
//
//    @NotNull
//    Integer step;
//
//    @NotNull
//    Integer group;
//
//    public CommentRequestDTO(Comment comment) {
//        this.id = comment.getId();
//        this.postId = comment.getPostId();
//        this.memberId = comment.getMemberId();
//        this.content = comment.getContent();
//        this.step = comment.getStep();
//        this.group = comment.getGroup();
//    }
//
//    // member
//
//    public static Comment toEntity(final CommentRequestDTO dto) {
//        return Comment.builder()
//                .postId(dto.getPostId())
//                .memberId(dto.memberId)
//                .content(dto.getContent())
//                .step(dto.getStep())
//                .title(dto.getTitle())
//                .gender(dto.getGender()) // enum
//                .personNum(dto.getPersonNum())
//                .content(dto.getContent())
//                .status(dto.getStatus()) // enum
//                .view(dto.getView())
////                .postImageList(dto.getPostImageList())
//                .category(dto.getCategory()) // enum
////                .postHashtagList(dto.getPostHashtagList())
//                .build();
//    }
//}

package com.backend.together.domain.comment.dto;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.dto.PostRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CommentRequestDTO {

    private Long post;
    private String content;
    private Long parent;
    private Long writer;



}

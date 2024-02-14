package com.backend.together.domain.comment.dto;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.member.dto.MemberDto;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDTO{


    private Long id;
    private String content;
    private CommentMemberDTO writer;
    private LocalDateTime createdAt;
    private List<CommentResponseDTO> children = new ArrayList<>();

    public CommentResponseDTO(Long id, String content,LocalDateTime createdAt, CommentMemberDTO writer) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;


    }

    public static CommentResponseDTO convertCommentToDTO(Comment comment) {
        return comment.getIsDeleted() ?
                new CommentResponseDTO(comment.getId(), "삭제된 댓글입니다.", comment.getCreatedAt(), null) :
                new CommentResponseDTO(comment.getId(), comment.getContent(), comment.getCreatedAt(), new CommentMemberDTO(comment.getWriter())); // comment.getWriter()
    }
}

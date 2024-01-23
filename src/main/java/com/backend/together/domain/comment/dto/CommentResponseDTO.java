package com.backend.together.domain.comment.dto;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.member.dto.MemberDto;
import com.backend.together.domain.member.entity.MemberEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDTO{
    private Long id;
    private String content;
    private Long writer;
    private List<CommentResponseDTO> children = new ArrayList<>();

    public CommentResponseDTO(Long id, String content, Long writer) {
        this.id = id;
        this.content = content;
        this.writer = writer;
    }

    public static CommentResponseDTO convertCommentToDTO(Comment comment) {
        return comment.getIsDeleted() ?
                new CommentResponseDTO(comment.getId(), "삭제된 댓글입니다.", null) :
                new CommentResponseDTO(comment.getId(), comment.getContent(), comment.getWriter().getMemberId());
    }
}

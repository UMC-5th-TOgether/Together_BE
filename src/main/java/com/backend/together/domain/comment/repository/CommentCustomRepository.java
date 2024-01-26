package com.backend.together.domain.comment.repository;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.comment.dto.CommentResponseDTO;

import java.util.List;
import java.util.Optional;

public interface CommentCustomRepository {
    List<CommentResponseDTO> findByPostId(Long id);

    Optional<Comment> findCommentByIdWithParent(Long id);
}

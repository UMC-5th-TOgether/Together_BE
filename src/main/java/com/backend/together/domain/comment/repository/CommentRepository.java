package com.backend.together.domain.comment.repository;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.comment.dto.CommentResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT comment " +
            "FROM Comment comment " +
            "LEFT JOIN FETCH comment.parent " +
            "WHERE comment.post.id = :id " +
            "ORDER BY comment.parent.id ASC NULLS FIRST, comment.createdAt ASC")
    List<Comment> findCommentsWithParentFetch(@Param("id") Long id);

    public Optional<Comment> findCommentById(Long commentId);

}

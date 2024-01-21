package com.backend.together.domain.comment.repository;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}

package com.backend.together.domain.comment.service;

import com.backend.together.domain.comment.Comment;
import org.hibernate.sql.Update;

import java.util.List;

public interface CommentService {
    public List<Comment> retrieveCommentsWithParent(Long postId);

    public Comment create(Comment comment);

    public void delete(Long commentId);

    public Comment update();

    public Comment retrieve();


}

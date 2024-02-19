package com.backend.together.domain.comment.service;

import com.backend.together.domain.comment.Comment;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    public List<Comment> retrieveCommentsWithParent(Long postId);

    public Comment create(Comment comment);

    public void delete(Long commentId);

    public Comment update();

    public Comment retrieve();

    Page<Comment> getCommentByMemberId(Long memberId, Integer page);

}

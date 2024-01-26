package com.backend.together.domain.comment.service;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository repository;

    @Override
    public Comment create(Comment c) {
        // 부모 댓글이 있고 저장되지 않았다면 먼저 저장
        Comment parentComment = c.getParent();
        // 부모 댓글이 있고, 부모 댓글의 ID가 null이 아니면 연결을 유지하고 저장
        if (parentComment != null && parentComment.getId() != null) {
            // 부모 댓글이 이미 저장되어 있는 경우, 해당 댓글을 참조하도록 설정
            c.setParent(repository.findById(parentComment.getId()).orElse(null));
        }

        repository.save(c);

        return c;
    }

    public List<Comment> retrieveCommentsWithParent(Long postId) {
        return repository.findCommentsWithParentFetch(postId);

    }
    public Comment delete() {
        return null;
    }

    @Override
    public Comment update() {
        return null;
    }

    @Override
    public Comment retrieve() {
        return null;
    }
}

package com.backend.together.domain.comment.service;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.comment.repository.CommentRepository;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public void delete(Long commentId) {

        // parent is null, 나의 자식이 없음 -> 삭제\
        // parent is null, 나의 자식이 있음 -> 문구만 변경
        // parent is true, 나의 자식이 없음 -> 삭제
        // parent is true, 나의 자식이 있음 -> 문구만 변경
        // => 자식이 있으면 문구만 변경/자식이 없으면 삭제
        // CommentServiceImpl.java
        Optional<Comment> optionalComment = repository.findById(commentId);
        Comment comment = optionalComment.orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + commentId));

        if (comment.getId() != null) {
            if (!comment.getChildren().isEmpty()) { //비어있지 안흐면
                // 부모 엔터티의 자식 참조를 삭제
//                comment.getChildren().forEach(child -> child.setParent(null));
                // 부모 엔터티를 삭제하지 않고 상태만 변경
                comment.changeIsDeleted(true);
                // 변경된 상태를 저장
                repository.save(comment);
            } else { // 비워져 있으면
                // 자식이 없으면 삭제
                repository.delete(comment);
            }
        } else {
            // ID가 null인 Comment 엔터티는 삭제할 수 없음
            throw new CustomHandler(ErrorStatus._BAD_REQUEST);
        }

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

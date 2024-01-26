package com.backend.together.domain.comment.converter;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.comment.dto.CommentRequestDTO;
import com.backend.together.domain.comment.repository.CommentRepository;
import com.backend.together.domain.comment.service.CommentService;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.repository.PostRepository;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class CommentConverter {

    @Autowired@Lazy
    PostRepository repository;
    @Autowired@Lazy
    MemberRepository memberRepository;
    @Autowired
    CommentRepository commentRepository;

    public Comment CommentToEntity(CommentRequestDTO dto) {
        // 부모 필드의 값이 -1이면 null로, 아니면 해당 ID의 Comment로 설정
        Comment parentComment = (dto.getParent() == -1) ? null : getComment(dto.getParent());

        Comment entity = Comment.builder()
                .content(dto.getContent())
                .post(getPost(dto.getPost()))
                .isDeleted(false)
                .parent(parentComment)
                .writer(getMember(dto.getWriter()))
                .build();

        return entity;
    }

    public Comment getComment(Long parentId) {
        if (parentId != null && parentId != -1) {
            // parentId가 유효하면 해당 ID의 Comment를 찾아서 반환
            Optional<Comment> comment = commentRepository.findById(parentId);// 적절한 Comment 조회 로직 추가
            return comment.orElse(null); // 원하는 로직에 따라 수정
        }
        return null; // parentId가 -1이거나 null인 경우, null 반환
    }

    public Post getPost(Long postId) {
        Optional<Post> post = repository.findById(postId);
        return post.orElse(null);
    }

    public MemberEntity getMember(Long memberId) {
        Optional<MemberEntity> member = memberRepository.findById(memberId);
        return member.orElse(null);
    }
}


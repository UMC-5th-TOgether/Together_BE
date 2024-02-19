package com.backend.together.domain.home.repository;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.Post;
import com.backend.together.global.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeRepository extends JpaRepository<Post, Long> {
    List<Post> findTop8ByCategoryAndMemberNotInOrderByViewDesc(Category category, List<MemberEntity> blockedList);
    List<Post> findTop8ByCategoryOrderByViewDesc(Category category);
}

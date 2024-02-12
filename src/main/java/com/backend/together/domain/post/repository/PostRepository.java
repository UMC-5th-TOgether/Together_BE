package com.backend.together.domain.post.repository;

//import com.backend.domain.mapping.PostCategory;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.global.enums.Category;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import com.backend.together.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> { // extends JpaRepository<Post, Long>

    List<Post> findPostByMember(MemberEntity member);
    List<Post> findPostByTitleContainingOrContentContaining(String keyword1, String keyword2);

    List<Post> findPostByCategory(Category category);

    List<Post> findPostByGender(Gender gender);

    List<Post> findPostByStatus(PostStatus status);

    //    List<Post> findPostByPostHashtagList_Hashtag_Name(String hashtag);
    Optional<Post> findById(Long id);

    List<Post> findPostByStatus(String status);

    Page<Post> findAllByCategory(Category category, Pageable pageable);

    Page<Post> findAllByCategoryAndMemberNotIn(Category category, List<MemberEntity> blockedList, Pageable pageable);

    Page<Post> findAllByMember(MemberEntity member, Pageable pageable);
}

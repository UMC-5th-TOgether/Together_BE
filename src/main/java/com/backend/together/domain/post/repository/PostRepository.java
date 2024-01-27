package com.backend.together.domain.post.repository;

//import com.backend.domain.mapping.PostCategory;
import com.backend.together.domain.category.Category;
import com.backend.together.domain.member.enums.Gender;
import com.backend.together.domain.member.enums.PostStatus;
import com.backend.together.domain.post.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> { // extends JpaRepository<Post, Long>

    List<Post> findPostByMemberId(Long memberId);
    List<Post> findPostByTitleContainingOrContentContaining(String keyword1, String keyword2);

    List<Post> findPostByCategory(Category category);

    List<Post> findPostByGender(Gender gender);

    List<Post> findPostByStatus(PostStatus status);

    //    List<Post> findPostByPostHashtagList_Hashtag_Name(String hashtag);
    Optional<Post> findPostById(Long postId);

    List<Post> findPostByStatus(String status);
    @Modifying
    @Query("update Post p set p.view = p.view + 1 where p.id = :id")
    int updateView(Long id);

}

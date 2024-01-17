package com.backend.together.domain.post.repository;

//import com.backend.domain.mapping.PostCategory;
import com.backend.together.domain.category.Category;
import com.backend.together.domain.enums.Gender;
import com.backend.together.domain.enums.PostStatus;
import com.backend.together.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> { // extends JpaRepository<Post, Long>

    List<Post> findPostByMemberId(Long memberId);
    List<Post> findPostByTitleContainingOrContentContaining(String keyword1, String keyword2);

    List<Post> findPostByCategory(Category category);

    List<Post> findPostByGender(Gender gender);

    List<Post> findPostByStatus(PostStatus status);

//    List<Post> findPostByPostHashtagList_Hashtag_Name(String hashtag);

    List<Post> findPostByStatus(String status);

}

package com.backend.together.domain.post.repository;

//import com.backend.domain.mapping.PostCategory;
import com.backend.together.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> { // extends JpaRepository<Post, Long>

//    Post save(Post post);
//
//    Optional<Post> findById(Long id);
//
//    List<Post>  findByTitle(String title);
//
//    List<Post> findByMember(Long id);
//
//    List<Post> findByCategory(Category category);
//
//    List<Post> findByHashtag(Hashtag hashtag);
//
//    List<Post> findAll();
}

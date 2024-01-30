package com.backend.together.domain.post.repository;

import com.backend.together.domain.post.Hashtag;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.mapping.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostHashtagRepository  extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findAllByPost(Post post);
    @Query("SELECT h.name FROM PostHashtag ph JOIN ph.hashtag h WHERE ph.post = :post")
    List<String> findHashtagNamesByPostId(@Param("post") Post post);

}

package com.backend.together.domain.post.repository;

import com.backend.together.domain.post.Hashtag;
import com.backend.together.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository  extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findHashtagByName(String hashtag);


}

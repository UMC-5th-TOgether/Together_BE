package com.backend.together.domain.post.repository;

import com.backend.together.domain.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository  extends JpaRepository<PostImage, Long> {
}

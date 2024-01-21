package com.backend.together.domain.post.service;

import com.backend.together.domain.category.Category;
import com.backend.together.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public Post createPost(Post post);

    public void deletePost(Long postId);

    public Post updatePost(Post post);

    public Optional<Post> retrievePostById(Long postId);

    public List<Post> retrievePostsByKeyword(String keyword);

    public void updateView(Long id);

            // ...해시태그 검색, 카테고리 검색 등.....


}

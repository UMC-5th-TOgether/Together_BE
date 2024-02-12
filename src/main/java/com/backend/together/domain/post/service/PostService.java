package com.backend.together.domain.post.service;

import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.dto.PostRequestDTO;
import com.backend.together.global.enums.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post createPost(PostRequestDTO requestDTO, Long memberId);

    void deletePost(Long postId, Long memberId);

    Post updatePost(Post post);

    Optional<Post> retrievePostById(Long postId);

    List<Post> retrievePostsByKeyword(String keyword);

    void updateView(Post post);

    Page<Post> getPostByCategorySort(String category, String sortBy, Integer page);

    Page<Post> getPostByMemberId(Long memberId, Integer page);

            // ...해시태그 검색, 카테고리 검색 등.....


}

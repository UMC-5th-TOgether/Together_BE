package com.backend.together.domain.home.service;

import com.backend.together.domain.post.Post;

import java.util.List;

public interface HomeService {
    List<Post> getPostListForGuestHome(String category);
    List<Post> getPostListForHome(String category, Long userId);
}

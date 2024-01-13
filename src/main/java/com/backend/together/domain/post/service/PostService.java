package com.backend.together.domain.post.service;

import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    public String testService() {
        Post post = Post.builder().title("first post").build();
        repository.save(post);
        Post savedPost = repository.findById(post.getId()).get();
        return savedPost.getTitle();
    }
//    public List<Post> create(final Post post) {
//        // validations
//        if (post == null) {
//            //log.warn("Entity cannot be null");
//        }
//        repository.save(post);
//    }
}

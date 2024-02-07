package com.backend.together.domain.post.service;

import com.backend.together.global.enums.Category;
import com.backend.together.domain.post.repository.HashtagRepository;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private PostHashtagService postHashtagService;
    @PersistenceContext
    private EntityManager entityManager;

    public String testService() {
        Post post = Post.builder().title("first post").build();
        repository.save(post);
        Post savedPost = repository.findById(post.getId()).get();
        return savedPost.getTitle();
    }

    @Override
    public void createPost(Post post) {
        //validate


        repository.save(post);





    }
//    public void saveHashtag(Question question, List<String> tagNames) {
//
//        if(tagNames.size() == 0) return;
//
//        tagNames.stream()
//                .map(hashtag ->
//                        hashtagService.findByTagName(hashtag)
//                                .orElseGet(() -> hashtagService.save(hashtag)))
//                .forEach(hashtag -> mapHashtagToQuestion(question, hashtag));
//    }

    @Override
    public void deletePost(Long postId) {
        //validate
        repository.deleteById(postId);
    }

    @Override
    public Post updatePost(Post post) {
        // validate
        return repository.save(post);
    }
    public List<Post> retrieve(){
        System.out.println(repository.findAll());
        return repository.findAll();
    }

    public List<Post> retrievePostByMemberId(Long id){
        System.out.println(repository.findAll());
        return repository.findPostByMemberId(id);
    }

    @Override
    public Optional<Post> retrievePostById(Long postId) {
        //        if(retrievedPost == null) {} => validate

        return repository.findById(postId); // null은 controller에서 처리
    }

    @Override
    public List<Post> retrievePostsByKeyword(String keyword) {
        List<Post> postByTitleContainingOrContentContaining = repository.findPostByTitleContainingOrContentContaining(keyword, keyword);
        return postByTitleContainingOrContentContaining;
    }


    public List<Post> retrievePostsByCategory(Category category) {
        List<Post> postByCategory = repository.findPostByCategory(category);
        return postByCategory;
    }
    public List<Post> retrievePostsByGender(Gender gender) {
        List<Post> postByGender = repository.findPostByGender(gender);
        return postByGender;
    }
    public List<Post> retrievePostsByStatus(PostStatus status) {
        List<Post> postByStatus = repository.findPostByStatus(status);
        return postByStatus;
    }


    /* Views Counting */
    /* Views Counting */
    @Override
    @Transactional
    public void updateView(Long id) {
        repository.updateView(id);
    }


    private static void validate(Post post) {
        if(post == null){
            log.warn("Entity cannot be null.");
        }

        if(post.getMemberId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }


}

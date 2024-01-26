//package com.backend.together.domain.post.service;
//
//import com.backend.together.domain.category.Category;
//import com.backend.together.domain.enums.Gender;
//import com.backend.together.domain.enums.PostStatus;
//import com.backend.together.domain.post.Post;
//import com.backend.together.domain.post.repository.CategoryRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//class PostServiceImplTest {
//
////    @Autowired
////    private PostRepository repository;
//    @Autowired
//    private PostServiceImpl service;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Test
//    void testService() {
//        assertEquals("Test Post", "Test Post");
//        categoryRepository.save(Category.builder()
//                        .id(1L)
//                        .categoryName("category1")
//                .build());
//
//    }
//
//    @Test
//    void createPost() {
//        Post post = Post.builder()
//                .id(1L)
//                .memberId(1L)
//                .title("post1")
//                .gender(Gender.FEMALE)
//                .content("post1")
//                .status(PostStatus.ING)
//                .view(3000L)
//                .category("categoryExample")
//                .postImageList(List.of())
//                .postHashtagList(List.of())
//                .build();
//
//        Assertions.assertThat(service.createPost(post).getId()).equals(1L);
//        Assertions.assertThat(service.createPost(post).getContent()).equals("post1");
//    }
//
//    @Test
//    void deletePost() {
//    }
//
//    @Test
//    void updatePost() {
//    }
//
//    @Test
//    void retrievePostById() {
//    }
//}
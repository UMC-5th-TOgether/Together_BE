//package com.backend.domain.post.repository;
//
//import com.backend.domain.category.Category;
//import com.backend.domain.post.Post;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class MemoryPostRepositoryTest {
//
//    MemoryPostRepository repository = new MemoryPostRepository();
//
//    @AfterEach
//    public void afterEach() {
//        repository.clearStore();
//    }
//
//    @Test
//    void save() {
//        Post post = new Post();
//        post.setTitle("title1");
//
//        repository.save(post);
//        Post result = repository.findById(post.getPostId()).get();
//
//        assertThat(result).isEqualTo(post);
//
//    }
//
//    @Test
//    void findById() {
//        Post post = new Post();
//        post.setTitle("title1");
//
//        repository.save(post);
//        Post result = repository.findById(post.getPostId()).get();
//
//        assertThat(result.getPostId()).isEqualTo(post.getPostId());
//    }
//
//    @Test
//    void findByTitle() {
//        Post post = new Post();
//        post.setTitle("title1");
//        Post post2 = new Post();
//        post2.setTitle("title1");
//        Post post3 = new Post();
//        post3.setTitle("title1");
//
//        repository.save(post);
//        repository.save(post2);
//        repository.save(post3);
//
//        List<Post> postListByTitle= repository.findByTitle("title1");
//        assertThat(postListByTitle.size()).isEqualTo(3);
//    }
//
//    @Test
//    void findByMember() {
//        Post post1 = new Post();
//        post1.setMemberId(1L);
//        Post post2 = new Post();
//        post2.setMemberId(2L);
//        Post post3 = new Post();
//        post3.setMemberId(3L);
//        Post post4 = new Post();
//        post4.setMemberId(1L);
//        Post post5 = new Post();
//        post5.setMemberId(1L);
//
//        repository.save(post1);
//        repository.save(post2);
//        repository.save(post3);
//        repository.save(post4);
//        repository.save(post5);
//
//        List<Post> postList = repository.findByMember(1L);
//
//        assertThat(postList.size()).isEqualTo(3);
//
//
//    }
//
//    @Test
//    void findByCategory_single_category() {
//
//        // Category 인스턴스 생성
//        Category category1 = Category.builder()
//                .categoryId(1L)
//                .categoryName("category1")
//                .build();
//        // Category LIst 생성
//        List<Category> categoryList = new ArrayList<>();
//        categoryList.add(category1);
//        // Post 인스턴스 생성 및 Category 할당
//        Post post1 = new Post();
//        post1.setCategoryList(categoryList); // 가정: Post 클래스에 setCategory 메소드가 있다고 가정
//
//
//        // 리포지토리에 Post 인스턴스 저장
//        repository.save(post1);
//
//        // 카테고리에 따라 Post 검색
//        List<Post> postListByCategory = repository.findByCategory(category1);
//
//        // 검증: 카테고리가 'category1'인 Post가 2개인지 확인
//        assertThat(postListByCategory.size()).isEqualTo(1);
//    }
//    @Test
//    void findByCategory_many_category() {
//
//        // Category 인스턴스 생성
//        Category category1 = Category.builder()
//                .categoryId(1L)
//                .categoryName("category1")
//                .build();
////        Category category2 = Category.builder()
////                .categoryId(2L)
////                .categoryName("category2")
////                .build();
//        // Category LIst 생성
//        List<Category> categoryList = new ArrayList<>();
//        categoryList.add(category1);
////        categoryList.add(category2);
//
//        // Post 인스턴스 생성 및 Category 할당
//        Post post1 = new Post();
//        post1.setCategoryList(categoryList);
//        Post post2 = new Post();
//        post2.setCategoryList(categoryList);
//
//        // 리포지토리에 Post 인스턴스 저장
//        repository.save(post1);
//        repository.save(post2);
//
//        // 카테고리에 따라 Post 검색
//        List<Post> postListByCategory = repository.findByCategory(category1);
//
//        // 검증: 카테고리가 'category1'인 Post가 2개인지 확인
//        assertThat(postListByCategory.size()).isEqualTo(2);
//    }
//    @Test
//    void findByHashtag() {
//
//    }
//
//    @Test
//    void findAll() {
//        Post post1 = new Post();
//        repository.save(post1);
//
//        Post post2 = new Post();
//        repository.save(post2);
//
//        Post post3 = new Post();
//        repository.save(post3);
//
//        List<Post> result = repository.findAll();
//        assertThat(result.size()).isEqualTo(3);
//    }
//
//    @Test
//    void clearStore() {
//    }
//}
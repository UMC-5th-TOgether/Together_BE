//package com.backend.together.domain.post.repository;
//
////import com.backend.domain.mapping.PostCategory;
//import com.backend.together.domain.post.Post;
//
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//import java.util.function.Function;
//
//@Repository
//public class MemoryPostRepository implements PostRepository {
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends Post> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<Post> entities) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public Post getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Post getById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Post getReferenceById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends Post> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends Post> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends Post, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> S save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> List<S> saveAll(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public Optional<Post> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public List<Post> findAll() {
//        return null;
//    }
//
//    @Override
//    public List<Post> findAllById(Iterable<Long> longs) {
//        return null;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//
//    }
//
//    @Override
//    public void delete(Post entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Post> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public List<Post> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<Post> findAll(Pageable pageable) {
//        return null;
//    }
////    private static Map<Long, Post> store = new HashMap<>();
////    private static long sequence = 0L;
////
////    @Override
////    public Post save(Post post) {
////        post.setPostId(++sequence);
////        store.put(post.getPostId(), post);
////        return post;
////    }
////
////    @Override
////    public Optional<Post> findById(Long id) {
////        return Optional.ofNullable(store.get(id));
////    }
////
////    @Override
////    public List<Post> findByTitle(String title) {
////        return store.values().stream()
////                .filter(post -> post.getTitle().equals(title))
////                .collect(Collectors.toList());
////    }
////
////    @Override
////    public List<Post> findByMember(Long id) {
////        return store.values().stream()
////                .filter(post -> post.getMemberId().equals(id))
////                .collect(Collectors.toList());
////    }
////
////
////    @Override
////    public List<Post> findByCategory(Category category) {
////        return store.values().stream()
////                .filter(post -> post.getCategoryList().contains(category))
////                .collect(Collectors.toList());
////    }
////
////    // ??? Category인지 string인지 모르겠음
////
////
////    @Override
////    public List<Post> findByHashtag(Hashtag hashtag) {
////        return store.values().stream()
////                .filter(post -> post.getPostHashtagList().contains(hashtag))
////                .collect(Collectors.toList());
////    }
////
////    @Override
////    public List<Post> findAll() {
////        return new ArrayList<>(store.values());
////    }
////    public void clearStore() {
////        store.clear();
////    }
//}

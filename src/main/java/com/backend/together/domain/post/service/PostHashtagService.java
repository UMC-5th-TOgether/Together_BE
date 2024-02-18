package com.backend.together.domain.post.service;

import com.backend.together.domain.post.Hashtag;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.mapping.PostHashtag;
import com.backend.together.domain.post.repository.HashtagRepository;
import com.backend.together.domain.post.repository.PostHashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostHashtagService {

    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private PostHashtagRepository postHashtagRepository;
    public void saveHashtag(Post post, List<String> tagNames) {

        if(tagNames.size() == 0) return;

        tagNames.stream()
                .map(hashtag ->
                        hashtagService.findByTagName(hashtag)
                                .orElseGet(() -> hashtagService.save(hashtag)))
                .forEach(hashtag -> mapHashtagToPost(post, hashtag));
    }
    private Long mapHashtagToPost(Post post, Hashtag hashtag) { // post와 hashtag 중간 테이블 생성

        return postHashtagRepository.save(new PostHashtag(post, hashtag)).getId();
    }
    public List<PostHashtag> findHashtagListByPost(Post post) {

        return postHashtagRepository.findAllByPost(post);
    }

    public List<String> getHashtagToStringByPost(Post post) {
        return postHashtagRepository.findHashtagNamesByPostId(post);
    }

    public void updateHashtag(Post post, List<String> hashtags) {
        List<PostHashtag> hashtagList = postHashtagRepository.findAllByPost(post);

        if (!hashtagList.isEmpty()) {
            postHashtagRepository.deleteAll(hashtagList);
        }

        hashtags.stream()
                .map(hashtag ->
                        hashtagService.findByTagName(hashtag)
                                .orElseGet(() -> hashtagService.save(hashtag)))
                .forEach(hashtag -> mapHashtagToPost(post, hashtag));
    }
}

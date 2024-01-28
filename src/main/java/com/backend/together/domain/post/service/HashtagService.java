package com.backend.together.domain.post.service;

import com.backend.together.domain.post.Hashtag;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HashtagService {
    @Autowired
    private HashtagRepository hashtagRepository;

    public Optional<Hashtag> findByTagName(String tagName) {

        return hashtagRepository.findHashtagByName(tagName);
    }

    public Hashtag save(String tagName) {

        return hashtagRepository.save(
                Hashtag.builder()
                        .name(tagName)
                        .build());
    }
}

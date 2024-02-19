package com.backend.together.domain.post.service;

import com.backend.together.domain.matching.entity.MatchingImage;
import com.backend.together.domain.post.PostImage;
import com.backend.together.domain.post.repository.PostImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    public List<String> getPostImages(Long postId) {
        List<PostImage> postImages = postImageRepository.findAllByPostId(postId);

        List<String> imageUrlList = postImages.stream().map(PostImage::getImageUrl).toList();
        return imageUrlList;
    }
}

package com.backend.together.domain.matching.service;

import com.backend.together.domain.matching.entity.Matching;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MatchingImageService {
    void postMatchingImage(MultipartFile requestImage, Matching matching);
    List<String> getMatchingImages(Long matchingId);
}

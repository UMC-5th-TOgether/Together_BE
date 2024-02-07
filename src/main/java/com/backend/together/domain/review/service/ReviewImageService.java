package com.backend.together.domain.review.service;

import com.backend.together.domain.review.entity.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewImageService {
    void postReviewImage(MultipartFile image, Review review);
    List<String> getReviewImages(Long reviewId);
}

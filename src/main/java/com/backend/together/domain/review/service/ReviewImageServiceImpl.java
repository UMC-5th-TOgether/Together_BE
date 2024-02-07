package com.backend.together.domain.review.service;

import com.backend.together.domain.review.dto.ReviewRequestDTO;
import com.backend.together.domain.review.entity.Review;
import com.backend.together.domain.review.entity.ReviewImage;
import com.backend.together.domain.review.repository.ReviewImageRepository;
import com.backend.together.global.aws.s3.AmazonS3Manager;
import com.backend.together.global.aws.s3.Uuid;
import com.backend.together.global.aws.s3.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService{
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Override
    public void postReviewImage(MultipartFile image, Review review) {
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

        String imageUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid), image);

        reviewImageRepository.save(ReviewRequestDTO.toReviewImage(imageUrl, review));
    }

    @Override
    public List<String> getReviewImages(Long reviewId) {
        List<ReviewImage> images = reviewImageRepository.findAllByReviewId(reviewId);

        List<String> imageUrls = images.stream().map(ReviewImage::getImageUrl).toList();
        return imageUrls;
    }
}

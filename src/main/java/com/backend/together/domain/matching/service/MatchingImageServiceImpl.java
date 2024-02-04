package com.backend.together.domain.matching.service;

import com.backend.together.domain.matching.dto.MatchingRequestDTO;
import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.matching.entity.MatchingImage;
import com.backend.together.domain.matching.repository.MatchingImageRepository;
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
public class MatchingImageServiceImpl implements MatchingImageService{
    private final MatchingImageRepository matchingImageRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    @Override
    public void postMatchingImage(MultipartFile image, Matching matching) {
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

        String imageUrl = s3Manager.uploadFile(s3Manager.generateMatchingKeyName(savedUuid), image);

        matchingImageRepository.save(MatchingRequestDTO.toMatchingImage(imageUrl, matching));
    }

    @Override
    public List<String> getMatchingImages(Long matchingId) {
        List<MatchingImage> matchingImages = matchingImageRepository.findAllByMatchingId(matchingId);

        List<String> imageUrlList = matchingImages.stream().map(MatchingImage::getImageUrl).toList();
        return imageUrlList;
    }

}

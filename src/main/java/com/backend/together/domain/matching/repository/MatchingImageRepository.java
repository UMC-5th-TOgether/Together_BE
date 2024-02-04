package com.backend.together.domain.matching.repository;

import com.backend.together.domain.matching.entity.MatchingImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingImageRepository extends JpaRepository<MatchingImage, Long> {
    List<MatchingImage> findAllByMatchingId(Long matchingId);
}

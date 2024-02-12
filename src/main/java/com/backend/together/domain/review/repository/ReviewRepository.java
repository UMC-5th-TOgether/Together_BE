package com.backend.together.domain.review.repository;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.review.entity.Review;
import com.backend.together.global.enums.ReviewEmotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByReviewer(MemberEntity member);
    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewed = :member")
    Long countByReviewed(@Param("member") MemberEntity member);
    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewed = :member and r.emotion = :emotion")
    Long countGoodEmotionByReviewed(@Param("member") MemberEntity member, @Param("emotion") ReviewEmotion emotion);
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.reviewed = :member")
    Optional<Double> avgScoreByReviewed(@Param("member") MemberEntity member);
    Page<Review> findAllByReviewed(MemberEntity member, Pageable pageable);
}

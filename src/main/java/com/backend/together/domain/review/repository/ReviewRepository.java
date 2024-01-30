package com.backend.together.domain.review.repository;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByWriter(MemberEntity member);
}

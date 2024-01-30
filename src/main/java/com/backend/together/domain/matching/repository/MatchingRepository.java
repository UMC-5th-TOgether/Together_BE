package com.backend.together.domain.matching.repository;

import com.backend.together.global.enums.MatchingStatus;
import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, MemberEntity> {
    List<Matching> findAllByReceiverAndStatus(MemberEntity receiver, MatchingStatus status);
    List<Matching> findAllBySenderAndStatus(MemberEntity sender, MatchingStatus status);
    Optional<Matching> findById(Long id);
}

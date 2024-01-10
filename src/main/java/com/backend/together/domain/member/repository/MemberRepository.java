package com.backend.together.domain.member.repository;

import com.backend.together.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    Optional<MemberEntity> findByEmail (String email);
    Optional<MemberEntity> findByNickname (String nickname);
    Optional<MemberEntity> findByEmailAndNickname (String email, String nickname);

}

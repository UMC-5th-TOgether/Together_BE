package com.backend.together.domain.friend.repository;

import com.backend.together.domain.friend.entity.FriendList;
import com.backend.together.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendListRepository extends JpaRepository<FriendList, Long> {
    @Query("SELECT f FROM FriendList f WHERE f.member1.memberId = :id OR f.member2.memberId = :id")
    List<FriendList> findByMemberId(@Param("id") Long id);

    FriendList findByMember1AndMember2(MemberEntity member1, MemberEntity member2);
}

package com.backend.together.domain.chat.repository;

import com.backend.together.domain.chat.entity.ChatRoom;
import com.backend.together.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
//    List<ChatRoom> findAllByHostOrGuest(Long hostId, Long guestId);
    List<ChatRoom> findAllByHostOrGuest(MemberEntity host, MemberEntity guest);
//    Optional<ChatRoom> findChatRoomByMember(Long member1, Long member2);
    Optional<ChatRoom> findChatRoomByHostAndGuest(MemberEntity host, MemberEntity guest);
//    @Query("select c from ChatRoom c where (c.host.id = :member1 and c.guest.id = :member2) or (c.host.id =:member2 and c.guest.id=:member1)")
//    Optional<ChatRoom> findChatRoomByMember(@Param("member1") Long member1, @Param("member2") Long member2);

}
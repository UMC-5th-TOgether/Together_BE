package com.backend.together.domain.chat.repository;

import com.backend.together.domain.chat.entity.ChatRoom;
import com.backend.together.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
//    List<ChatRoom> findAllByHostOrGuest(MemberEntity host, MemberEntity guest);
//
//    @Query("select c from ChatRoom c where (c.host.id = :user1 and c.guest.id = :user2) or (c.host.id = :user2 and c.guest.id = :user1)")
//    Optional<ChatRoom> findChatRoomByUsers(@Param("user1") Long user1, @Param("user2") Long user2);
    ChatRoom findByUser1_NameAndUser2_Name(String name1, String name2);
    @Query("SELECT c FROM ChatRoom c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<ChatRoom> findChatRoomsByUserId(@Param("userId") Long userId);
}
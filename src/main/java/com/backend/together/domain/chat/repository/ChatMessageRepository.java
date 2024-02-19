package com.backend.together.domain.chat.repository;

import com.backend.together.domain.chat.entity.SocketMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<SocketMessage, Long> {
    List<SocketMessage> findAllByChatRoomId(Long chatRoomId);

    SocketMessage findTopByChatRoomIdOrderByTimeDesc(Long chatRoomId);

    void deleteByChatRoomId(Long chatRoomId);

    List<SocketMessage> findByChatRoomId(Long chatRoomId);

    List<SocketMessage> findAllByChatRoomIdAndSenderAndIsReadFalseAndTimeBefore(Long id, String sender, ZonedDateTime time);
}
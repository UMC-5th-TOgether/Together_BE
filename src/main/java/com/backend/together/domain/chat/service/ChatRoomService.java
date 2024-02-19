package com.backend.together.domain.chat.service;

import com.backend.together.domain.chat.dto.*;
import com.backend.together.domain.chat.entity.ChatRoom;
import com.backend.together.domain.chat.entity.SocketMessage;
import com.backend.together.domain.chat.repository.ChatMessageRepository;
import com.backend.together.domain.chat.repository.ChatRoomRepository;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ApiRes<ChatRoomResponseDto> createRoom(MemberEntity sentUser, MemberEntity receivedUser) {
        ChatRoom chatRoom = ChatRoom.builder()
                .user1(sentUser)
                .user2(receivedUser)
                .build();

        chatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .build();

        return ApiRes.successData(chatRoomResponseDto);
    }

    @Transactional
    public List<ChatRoomDto> findChatRoomsByUserId(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserId(userId);
        return chatRooms.stream()
                .map(chatRoom -> convertToDto(chatRoom, userId))
                .sorted(Comparator.comparing(ChatRoomDto::getLatestMessageTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    private ChatRoomDto convertToDto(ChatRoom chatRoom, Long userId) {
        MemberEntity oppositeUser = findOppositeUser(chatRoom, userId);
//        UserProfileEntity userProfileEntity = oppositeUser.getUserProfileEntity();
//        List<ProfileImageEntity> profileImageEntities = oppositeUser.getProfileImages();

//        String image = (profileImageEntities != null && !profileImageEntities.isEmpty())
//                ? profileImageEntities.get(0).getImage()
//                : null;

        SocketMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimeDesc(chatRoom.getId());

        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getId())
                .users(Arrays.asList(oppositeUser.getName()))
                .userId(oppositeUser.getMemberId())
//                .nickname(oppositeUser.getNickname())
//                .image(oppositeUser.getImage())
                .latestMessageId(latestMessage != null ? latestMessage.getId() : null)
                .latestMessage(latestMessage != null ? latestMessage.getMessage() : null)
                .latestMessageTime(latestMessage != null ? latestMessage.getTime().toString() : null)
                .build();
    }

    public ChatRoomInfoDto findChatRoomInfo(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new Error("NOT_FOUND_CHATROOM"));

        MemberEntity oppositeUser = findOppositeUser(chatRoom, userId);

        List<SocketMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<SocketMessageResponseDto> messageDtos = messages.stream()
                .map(message -> SocketMessageResponseDto.builder()
                        .chatRoomId(chatRoomId)
                        .sender(message.getSender())
                        .messageId(message.getId())
                        .message(message.getMessage())
                        .time(message.getTime())
                        .build())
                .collect(Collectors.toList());

        return ChatRoomInfoDto.builder()
                .userId(oppositeUser.getMemberId())
                .name(oppositeUser.getName())
//                .nickname(oppositeUser.getUserProfileEntity().getNickname())
//                .image(oppositeUser.getProfileImages().isEmpty() ? null : oppositeUser.getProfileImages().get(0).getImage())
                .messages(messageDtos)
                .build();
    }


    private MemberEntity findOppositeUser(ChatRoom chatRoom, Long id) {
        MemberEntity oppositeUser;
        if (chatRoom.getUser1().getMemberId().equals(id)) {
            oppositeUser = chatRoom.getUser2();
        } else if (chatRoom.getUser2().getMemberId().equals(id)) {
            oppositeUser = chatRoom.getUser1();
        } else {
            throw new Error("USER_NOT_FOUND");
        }
        return oppositeUser;
    }
}

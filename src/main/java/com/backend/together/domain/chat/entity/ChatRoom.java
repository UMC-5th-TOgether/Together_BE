package com.backend.together.domain.chat.entity;

import com.backend.together.domain.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//@Entity
//@NoArgsConstructor
//@Getter
//@Setter
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chatroom")
public class ChatRoom{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user1_id")
    private MemberEntity user1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user2_id")
    private MemberEntity user2;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SocketMessage> messages;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "host_id")
//    private MemberEntity host;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "guest_id")
//    private MemberEntity guest;
//    @Column(columnDefinition = "TIMESTAMP")
//    private LocalDateTime hostEntryTime; // 5/28 20시 10분 -> 6/3 20시 50분
//    @Column(columnDefinition = "TIMESTAMP")
//    private LocalDateTime guestEntryTime; // 5/28 20시 10분
//    private ChatRoom(MemberEntity host , MemberEntity guest,LocalDateTime time) {
//        this.host = host;
//        this.guest = guest;
//        this.hostEntryTime = time;
//        this.guestEntryTime = time;
//    }
//    public static ChatRoom of(MemberEntity host,MemberEntity guest,LocalDateTime time){
//        return new ChatRoom(host,guest,time);
//    }
//    public void updateHostEntryTime(LocalDateTime time){
//        this.hostEntryTime = time;
//    }
//    public void updateGuestEntryTime(LocalDateTime time){
//        this.guestEntryTime = time;
//    }
}
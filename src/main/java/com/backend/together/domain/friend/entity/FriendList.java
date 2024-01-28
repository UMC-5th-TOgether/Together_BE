package com.backend.together.domain.friend.entity;

import com.backend.together.global.enums.FriendStatus;
import com.backend.together.domain.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class FriendList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendList_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_member_id1", nullable = false)
    private MemberEntity member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_member_id2", nullable = false)
    private MemberEntity member2;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(" 'NORMAL' ")
    private FriendStatus status;
}

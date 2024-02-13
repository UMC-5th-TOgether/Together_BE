package com.backend.together.domain.matching.entity;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.global.common.BaseEntity;
import com.backend.together.global.enums.MatchingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private MemberEntity receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private MemberEntity sender;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(" 'PENDING' ")
    private MatchingStatus status;

    @Column(columnDefinition = "boolean default false")
    private Boolean isRead;

    public void updateMatchingStatus(MatchingStatus matchingStatus) {this.status = matchingStatus;}
    public void matchingRead(Boolean isRead) {this.isRead = isRead;}
}

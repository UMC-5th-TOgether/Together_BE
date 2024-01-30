package com.backend.together.domain.member.entity;

import com.backend.together.domain.member.entity.enums.Gender;
import com.backend.together.domain.member.entity.enums.MemberStatus;
import com.backend.together.domain.member.entity.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable=false)
    private String nickname;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=true)
    private String name;

    @Column(nullable=true)
    private String phoneNumber;

    @Column(nullable=true)
    private String image;

    @Column(nullable=true)
    private String age;

    //@Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private String gender;

    @Column(nullable=true)
    private String station;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'NORMAL'")
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable=true)
    private Provider provider; // 소셜로그인 구분을 위해

}

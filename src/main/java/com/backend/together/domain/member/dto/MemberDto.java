package com.backend.together.domain.member.dto;

import com.backend.together.domain.member.entity.enums.Gender;
import com.backend.together.domain.member.entity.enums.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberDto {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String station;
    private String nickname;
    private String age;
    private String gender;
    private String image;

}

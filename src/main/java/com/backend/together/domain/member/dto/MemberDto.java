package com.backend.together.domain.member.dto;

import com.backend.together.domain.member.entity.MemberEntity;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberDto {
    private String nickname;

    private String email;

    private String password;


}

package com.backend.together.domain.mypage.dto;

import com.backend.together.domain.member.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoDto {
    private String nickname;
    private String image;
    private String age;
    private String gender;
    private String station;
}

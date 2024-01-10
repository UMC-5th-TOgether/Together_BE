package com.backend.together.domain.mypage.dto;

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
    private Integer age;
    private String image;
}

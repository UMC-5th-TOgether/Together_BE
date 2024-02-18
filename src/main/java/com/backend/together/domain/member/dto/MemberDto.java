package com.backend.together.domain.member.dto;

import lombok.*;

import java.util.UUID;

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IsUserExistDTO{
        boolean exist;
        String nickname;
        String password;
        String email;

        public static MemberDto.IsUserExistDTO isUserExistDTO(boolean isExist, String nickname, String email){
            return IsUserExistDTO.builder()
                    .exist(isExist)
                    .nickname(nickname)
                    .email(email)
                    .password(UUID.randomUUID().toString())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SocialDTO{
        String email;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SocialResponseDTO{
        String token;

        public static MemberDto.SocialResponseDTO socialResponseDTO(TokenDto token){
            return SocialResponseDTO.builder()
                    .token(token.getToken())
                    .build();
        }
    }
}

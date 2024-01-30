package com.backend.together.domain.member.social;

import lombok.Data;

@Data
public class NaverProfile {
    public String resultcode;
    public String message;
    public Response response;

    @Data
    public class Response {
        public String id;
        public String nickname;
        public String profile_image;
        public String age;
        public String gender;// F: 여성, M: 남성, U: 확인 불가
        public String email;
        public String name;

    }



}

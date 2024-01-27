package com.backend.together.domain.member.social;

import lombok.Data;

@Data
public class NaverToken {
    public String access_token;
    public String refresh_token;
    public String token_type;
    public int expires_in;
    public String error;
    public String error_description;
}

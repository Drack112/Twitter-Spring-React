package com.gmail.drack.dto.response;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private AuthUserResponse user;
    private String token;
}

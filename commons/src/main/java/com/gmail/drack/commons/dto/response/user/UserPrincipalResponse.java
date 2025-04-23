package com.gmail.drack.commons.dto.response.user;

import lombok.Data;

@Data
public class UserPrincipalResponse {

    private Long id;
    private String email;
    private String activationCode;
}

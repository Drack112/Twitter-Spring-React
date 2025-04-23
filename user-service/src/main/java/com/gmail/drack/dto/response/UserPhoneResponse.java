package com.gmail.drack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPhoneResponse {

    private String phoneCode;
    private Long phoneNumber;
}

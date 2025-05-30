package com.gmail.drack.dto.request;

import com.gmail.drack.constants.Regexp;
import com.gmail.drack.constants.UserErrorMessage;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class AuthenticationRequest {

    @Email(regexp = Regexp.USER_EMAIL_REGEXP, message = UserErrorMessage.EMAIL_NOT_VALID)
    private String email;

    @NotBlank(message = UserErrorMessage.EMPTY_PASSWORD)
    @Size(min = 8, message = UserErrorMessage.SHORT_PASSWORD)
    private String password;
}

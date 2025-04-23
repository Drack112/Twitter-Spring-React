package com.gmail.drack.dto.request;

import com.gmail.drack.constants.Regexp;
import com.gmail.drack.constants.UserErrorMessage;
import lombok.Data;

import jakarta.validation.constraints.Email;

@Data
public class ProcessEmailRequest {

    @Email(regexp = Regexp.USER_EMAIL_REGEXP, message = UserErrorMessage.EMAIL_NOT_VALID)
    private String email;
}

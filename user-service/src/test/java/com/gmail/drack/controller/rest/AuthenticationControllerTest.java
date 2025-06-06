package com.gmail.drack.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.drack.commons.constants.HeaderConstants;
import com.gmail.drack.commons.constants.PathConstants;
import com.gmail.drack.constants.UserErrorMessage;
import com.gmail.drack.constants.UserSuccessMessage;
import com.gmail.drack.dto.request.*;
import com.gmail.drack.commons.util.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = {"/sql-test/clear-user-db.sql", "/sql-test/populate-user-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql-test/clear-user-db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    public void init() {
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(TestConstants.USER_EMAIL);
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/login - Login")
    public void login() throws Exception {
        authenticationRequest.setPassword(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.LOGIN)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/login - Should Email not valid")
    public void login_ShouldEmailNotValid() throws Exception {
        authenticationRequest.setEmail("notvalidemail@test");
        authenticationRequest.setPassword(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.LOGIN)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_VALID)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/login - Should password is empty")
    public void login_ShouldPasswordIsEmpty() throws Exception {
        authenticationRequest.setPassword(null);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.LOGIN)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.EMPTY_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/login - Should password less then 8 characters")
    public void login_ShouldPasswordLessThen8Characters() throws Exception {
        authenticationRequest.setPassword("test123");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.LOGIN)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.SHORT_PASSWORD)));
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/forgot/email - Find existing email")
    public void getExistingEmail() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.FORGOT_EMAIL)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/forgot/email - Should email not valid")
    public void getExistingEmail_ShouldEmailNotValid() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail("test2015@test");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.FORGOT_EMAIL)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_VALID)));
    }

    @Test
    @DisplayName("[404] POST /ui/v1/auth/forgot/email - Email not found")
    public void getExistingEmail_EmailNotFound() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail(TestConstants.NOT_VALID_EMAIL);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.FORGOT_EMAIL)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(UserErrorMessage.EMAIL_NOT_FOUND)));
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/forgot - Send password reset code")
    public void sendPasswordResetCode() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.FORGOT)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/forgot - Should email not valid")
    public void sendPasswordResetCode_ShouldEmailNotValid() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail("test2015@test");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.FORGOT)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_VALID)));
    }

    @Test
    @DisplayName("[404] POST /ui/v1/auth/forgot - Should email Not Found")
    public void sendPasswordResetCode_ShouldEmailNotFound() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail(TestConstants.NOT_VALID_EMAIL);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.FORGOT)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(UserErrorMessage.EMAIL_NOT_FOUND)));
    }

    @Test
    @DisplayName("[200] GET /ui/v1/auth/reset/1234567890 - Get user by reset code")
    public void getUserByPasswordResetCode() throws Exception {
        mockMvc.perform(get(PathConstants.UI_V1_AUTH + PathConstants.RESET_CODE, 1234567890))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("test2016@test.test"))
                .andExpect(jsonPath("$.fullName").value(TestConstants.FULL_NAME))
                .andExpect(jsonPath("$.username").value(TestConstants.USERNAME))
                .andExpect(jsonPath("$.location").value(TestConstants.LOCATION))
                .andExpect(jsonPath("$.about").value(TestConstants.ABOUT))
                .andExpect(jsonPath("$.website").value(TestConstants.WEBSITE))
                .andExpect(jsonPath("$.birthday").value(TestConstants.BIRTHDAY))
                .andExpect(jsonPath("$.registrationDate").value(TestConstants.REGISTRATION_DATE))
                .andExpect(jsonPath("$.tweetCount").value(TestConstants.TWEET_COUNT))
                .andExpect(jsonPath("$.avatar").value(TestConstants.AVATAR_SRC_1))
                .andExpect(jsonPath("$.wallpaper").value(TestConstants.WALLPAPER_SRC))
                .andExpect(jsonPath("$.profileCustomized").value(true))
                .andExpect(jsonPath("$.profileStarted").value(true));
    }

    @Test
    @DisplayName("[400] GET /ui/v1/auth/reset/test123 - Get user by reset code bad request")
    public void getUserByPasswordResetCode_BadRequest() throws Exception {
        mockMvc.perform(get(PathConstants.UI_V1_AUTH + PathConstants.RESET_CODE, "test123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is(UserErrorMessage.INVALID_PASSWORD_RESET_CODE)));
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/reset - Reset password")
    public void passwordReset() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail(TestConstants.USER_EMAIL);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UserSuccessMessage.PASSWORD_SUCCESSFULLY_CHANGED)));
    }

    @Test
    @DisplayName("[404] POST /ui/v1/auth/reset - Should user Not Found by email")
    public void passwordReset_ShouldUserNotFoundByEmail() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail(TestConstants.NOT_VALID_EMAIL);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_FOUND)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset - Should Email not valid")
    public void passwordReset_ShouldEmailNotValid() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail("notvalidemail@test");
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_VALID)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset - Should password be empty")
    public void passwordReset_ShouldPasswordBeEmpty() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail(TestConstants.USER_EMAIL);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.EMPTY_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset - Should password2 be empty")
    public void passwordReset_ShouldPassword2BeEmpty() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail(TestConstants.USER_EMAIL);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password2", is(UserErrorMessage.EMPTY_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset - Should password less then 8 characters")
    public void passwordReset_ShouldPasswordLessThen8Characters() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail(TestConstants.USER_EMAIL);
        passwordResetRequest.setPassword("qwerty");
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.SHORT_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset - Should password2 less then 8 characters")
    public void passwordReset_ShouldPassword2LessThen8Characters() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail(TestConstants.USER_EMAIL);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2("qwerty");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password2", is(UserErrorMessage.SHORT_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset - Should passwords not match")
    public void passwordReset_ShouldPasswordsNotMatch() throws Exception {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail(TestConstants.USER_EMAIL);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2("test1234");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.PASSWORDS_NOT_MATCH)));
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/reset/current - Current Password Reset")
    public void currentPasswordReset() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);

        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UserSuccessMessage.PASSWORD_SUCCESSFULLY_UPDATED)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset/current - Should current password is empty")
    public void currentPasswordReset_ShouldCurrentPasswordIsEmpty() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword("");
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.currentPassword", is(UserErrorMessage.EMPTY_CURRENT_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset/current - Should password is empty")
    public void currentPasswordReset_ShouldPasswordIsEmpty() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword(null);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.EMPTY_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset/current - Should password2 is empty")
    public void currentPasswordReset_ShouldPassword2IsEmpty() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2(null);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password2", is(UserErrorMessage.EMPTY_PASSWORD_CONFIRMATION)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset/current - Should password less then 8 characters")
    public void currentPasswordReset_ShouldPasswordLessThen8Characters() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword("test");
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.SHORT_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset/current - Should password2 less then 8 characters")
    public void currentPasswordReset_ShouldPassword2LessThen8Characters() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2("test");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password2", is(UserErrorMessage.SHORT_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset/current - Should current password reset not found")
    public void currentPasswordReset_ShouldCurrentPasswordResetNotFound() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword("qwerty123456");
        passwordResetRequest.setPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.currentPassword", is(UserErrorMessage.INCORRECT_PASSWORD)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/reset/current - Should passwords not match")
    public void currentPasswordReset_ShouldPasswordsNotMatch() throws Exception {
        CurrentPasswordResetRequest passwordResetRequest = new CurrentPasswordResetRequest();
        passwordResetRequest.setCurrentPassword(TestConstants.PASSWORD);
        passwordResetRequest.setPassword("qwerty123456");
        passwordResetRequest.setPassword2(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.RESET_CURRENT)
                .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                .content(mapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.PASSWORDS_NOT_MATCH)));
    }
}

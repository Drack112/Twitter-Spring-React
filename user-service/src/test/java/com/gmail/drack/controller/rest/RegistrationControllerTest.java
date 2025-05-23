package com.gmail.drack.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.drack.commons.constants.PathConstants;
import com.gmail.drack.constants.UserErrorMessage;
import com.gmail.drack.constants.UserSuccessMessage;
import com.gmail.drack.dto.request.AuthenticationRequest;
import com.gmail.drack.dto.request.ProcessEmailRequest;
import com.gmail.drack.dto.request.RegistrationRequest;
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
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private AuthenticationRequest authenticationRequest;
    private RegistrationRequest registrationRequest;

    @BeforeEach
    public void init() {
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(TestConstants.USER_EMAIL);

        registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("testtest@test.com");
        registrationRequest.setUsername("testtest");
        registrationRequest.setBirthday(TestConstants.BIRTHDAY);
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/registration/check - Check Email")
    public void checkEmail() throws Exception {
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CHECK)
                .content(mapper.writeValueAsString(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UserSuccessMessage.USER_DATA_CHECKED)));
    }

    @Test
    @DisplayName("[403] POST /ui/v1/auth/registration/check - Should user email is exist")
    public void checkEmail_ShouldUserEmailIsExist() throws Exception {
        registrationRequest.setEmail(TestConstants.USER_EMAIL);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CHECK)
                .content(mapper.writeValueAsString(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$", is(UserErrorMessage.EMAIL_HAS_ALREADY_BEEN_TAKEN)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/registration/check - Should email not valid")
    public void checkEmail_ShouldEmailNotValid() throws Exception {
        registrationRequest.setEmail("test2015@test");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CHECK)
                .content(mapper.writeValueAsString(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_VALID)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/registration/check - Should username is empty")
    public void checkEmail_ShouldUsernameIsEmpty() throws Exception {
        registrationRequest.setUsername(null);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CHECK)
                .content(mapper.writeValueAsString(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username", is(UserErrorMessage.BLANK_NAME)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/registration/check - Should username more then 50 characters")
    public void checkEmail_ShouldUsernameMoreThen50Characters() throws Exception {
        registrationRequest.setUsername("qwertqwertqwertqwertqwertqwertqwertqwertqwertqwert123");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CHECK)
                .content(mapper.writeValueAsString(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username", is(UserErrorMessage.NAME_NOT_VALID)));
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/registration/code - Send registration code")
    public void sendRegistrationCode() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CODE)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UserSuccessMessage.REGISTRATION_CODE_SENT)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/registration/code - Should email not valid")
    public void sendRegistrationCode_ShouldEmailNotValid() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail("test2015@test");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CODE)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_VALID)));
    }

    @Test
    @DisplayName("[404] POST /ui/v1/auth/registration/code - User not found")
    public void sendRegistrationCode_ShouldUserNotFound() throws Exception {
        ProcessEmailRequest request = new ProcessEmailRequest();
        request.setEmail(TestConstants.NOT_VALID_EMAIL);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CODE)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(UserErrorMessage.USER_NOT_FOUND)));
    }

    @Test
    @DisplayName("[200] GET /ui/v1/auth/registration/activate/1234567890 - Check registration code")
    public void checkRegistrationCode() throws Exception {
        mockMvc.perform(get(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_ACTIVATE_CODE, 1234567890))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UserSuccessMessage.USER_SUCCESSFULLY_ACTIVATED)));
    }

    @Test
    @DisplayName("[404] GET /ui/v1/auth/registration/activate/test - Registration code not found")
    public void checkRegistrationCode_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_ACTIVATE_CODE, "test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(UserErrorMessage.ACTIVATION_CODE_NOT_FOUND)));
    }

    @Test
    @DisplayName("[200] POST /ui/v1/auth/registration/confirm - End registration")
    public void endRegistration() throws Exception {
        authenticationRequest.setPassword(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CONFIRM)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(TestConstants.USER_ID))
                .andExpect(jsonPath("$.user.email").value(TestConstants.USER_EMAIL))
                .andExpect(jsonPath("$.user.fullName").value(TestConstants.FULL_NAME))
                .andExpect(jsonPath("$.user.username").value(TestConstants.USERNAME))
                .andExpect(jsonPath("$.user.location").value(TestConstants.LOCATION))
                .andExpect(jsonPath("$.user.about").value(TestConstants.ABOUT))
                .andExpect(jsonPath("$.user.website").value(TestConstants.WEBSITE))
                .andExpect(jsonPath("$.user.birthday").value(TestConstants.BIRTHDAY))
                .andExpect(jsonPath("$.user.registrationDate").value(TestConstants.REGISTRATION_DATE))
                .andExpect(jsonPath("$.user.tweetCount").value(TestConstants.TWEET_COUNT))
                .andExpect(jsonPath("$.user.avatar").value(TestConstants.AVATAR_SRC_1))
                .andExpect(jsonPath("$.user.wallpaper").value(TestConstants.WALLPAPER_SRC))
                .andExpect(jsonPath("$.user.profileCustomized").value(true))
                .andExpect(jsonPath("$.user.profileStarted").value(true));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/registration/confirm - Should email not valid")
    public void endRegistration_ShouldEmailNotValid() throws Exception {
        authenticationRequest.setEmail("test2015@test");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CONFIRM)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is(UserErrorMessage.EMAIL_NOT_VALID)));
    }

    @Test
    @DisplayName("[400] POST /ui/v1/auth/registration/confirm - Should short password")
    public void endRegistration_ShouldShortPassword() throws Exception {
        authenticationRequest.setPassword("123");
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CONFIRM)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password", is(UserErrorMessage.SHORT_PASSWORD)));
    }

    @Test
    @DisplayName("[404] POST /ui/v1/auth/registration/confirm - Should user Not Found by email")
    public void endRegistration_ShouldUserNotFound() throws Exception {
        authenticationRequest.setEmail(TestConstants.NOT_VALID_EMAIL);
        authenticationRequest.setPassword(TestConstants.PASSWORD);
        mockMvc.perform(post(PathConstants.UI_V1_AUTH + PathConstants.REGISTRATION_CONFIRM)
                .content(mapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(UserErrorMessage.USER_NOT_FOUND)));
    }
}

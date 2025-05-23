package com.gmail.drack.service;

import com.gmail.drack.UserServiceTestHelper;
import com.gmail.drack.commons.exception.InputFieldException;
import com.gmail.drack.constants.UserErrorMessage;
import com.gmail.drack.constants.UserSuccessMessage;
import com.gmail.drack.dto.request.AuthenticationRequest;
import com.gmail.drack.commons.event.SendEmailEvent;
import com.gmail.drack.commons.exception.ApiRequestException;
import com.gmail.drack.model.User;
import com.gmail.drack.model.UserRole;
import com.gmail.drack.repository.projection.AuthUserProjection;
import com.gmail.drack.repository.projection.UserCommonProjection;
import com.gmail.drack.commons.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Optional;

import static com.gmail.drack.broker.producer.SendEmailProducer.toSendPasswordResetEmailEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthenticationServiceImplTest extends AbstractServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    private final BindingResult bindingResult = mock(BindingResult.class);
    private final UserCommonProjection userCommonProjection = UserServiceTestHelper.createUserCommonProjection();
    private final AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();

    @Test
    public void getAuthenticatedUser_ShouldReturnAuthenticatedUser() {
        User user = new User();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        assertEquals(user, authenticationService.getAuthenticatedUser());
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    public void getAuthenticatedUser_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.getAuthenticatedUser());
        assertEquals(UserErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void login_ShouldReturnAuthenticatedUser() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        Map<String, Object> userMap = Map.of("user", authUserProjection, "token", TestConstants.AUTH_TOKEN);
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, AuthUserProjection.class))
                .thenReturn(Optional.of(authUserProjection));
        when(jwtProvider.createToken(TestConstants.USER_EMAIL, UserRole.USER.name())).thenReturn(TestConstants.AUTH_TOKEN);
        assertEquals(userMap, authenticationService.login(request, bindingResult));
        verify(userRepository, times(1)).getUserByEmail(TestConstants.USER_EMAIL, AuthUserProjection.class);
        verify(jwtProvider, times(1)).createToken(TestConstants.USER_EMAIL, UserRole.USER.name());
    }

    @Test
    public void login_ShouldThrowUserNotFoundException() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        BindingResult bindingResult = mock(BindingResult.class);
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, AuthUserProjection.class))
                .thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.login(request, bindingResult));
        assertEquals(UserErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getUserByToken_ShouldReturnAuthUserProjection() {
        Map<String, Object> userMap = Map.of("user", authUserProjection, "token", TestConstants.AUTH_TOKEN);
        when(userRepository.getUserById(TestConstants.USER_ID, AuthUserProjection.class))
                .thenReturn(Optional.of(authUserProjection));
        when(jwtProvider.createToken(TestConstants.USER_EMAIL, UserRole.USER.name())).thenReturn(TestConstants.AUTH_TOKEN);
        assertEquals(userMap, authenticationService.getUserByToken());
        verify(userRepository, times(1)).getUserById(TestConstants.USER_ID, AuthUserProjection.class);
        verify(jwtProvider, times(1)).createToken(TestConstants.USER_EMAIL, UserRole.USER.name());
    }

    @Test
    public void getUserByToken_ShouldThrowUserNotFoundException() {
        when(userRepository.getUserById(TestConstants.USER_ID, AuthUserProjection.class))
                .thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.getUserByToken());
        assertEquals(UserErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getExistingEmail_ShouldReturnSuccessMessage() {
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class))
                .thenReturn(Optional.of(userCommonProjection));
        assertEquals(UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND,
                authenticationService.getExistingEmail(TestConstants.USER_EMAIL, bindingResult));
        verify(userRepository, times(1)).getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class);
    }

    @Test
    public void getExistingEmail_ShouldThrowEmailNotFoundException() {
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class))
                .thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.getExistingEmail(TestConstants.USER_EMAIL, bindingResult));
        assertEquals(UserErrorMessage.EMAIL_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void sendPasswordResetCode_ShouldReturnSuccessMessage() {
        SendEmailEvent sendEmailEvent = toSendPasswordResetEmailEvent(
                userCommonProjection.getEmail(),
                userCommonProjection.getFullName(),
                TestConstants.PASSWORD_RESET_CODE);
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class))
                .thenReturn(Optional.of(userCommonProjection));
        when(userRepository.getPasswordResetCode(TestConstants.USER_ID)).thenReturn(TestConstants.PASSWORD_RESET_CODE);
        assertEquals(UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND,
                authenticationService.sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult));
        verify(userRepository, times(1)).getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class);
        verify(userRepository, times(1)).getPasswordResetCode(TestConstants.USER_ID);
        verify(sendEmailProducer, times(1)).sendEmail(sendEmailEvent);
    }

    @Test
    public void sendPasswordResetCode_ShouldThrowEmailNotFoundException() {
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class))
                .thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult));
        assertEquals(UserErrorMessage.EMAIL_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getUserByPasswordResetCode_ShouldReturnAuthUserProjection() {
        when(userRepository.getByPasswordResetCode(TestConstants.PASSWORD_RESET_CODE))
                .thenReturn(Optional.of(authUserProjection));
        assertEquals(authUserProjection, authenticationService.getUserByPasswordResetCode(TestConstants.PASSWORD_RESET_CODE));
        verify(userRepository, times(1)).getByPasswordResetCode(TestConstants.PASSWORD_RESET_CODE);
    }

    @Test
    public void getUserByPasswordResetCode_ShouldThrowEmailNotFoundException() {
        when(userRepository.getByPasswordResetCode(TestConstants.PASSWORD_RESET_CODE))
                .thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.getUserByPasswordResetCode(TestConstants.PASSWORD_RESET_CODE));
        assertEquals(UserErrorMessage.INVALID_PASSWORD_RESET_CODE, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void passwordReset_ShouldReturnSuccessMessage() {
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class))
                .thenReturn(Optional.of(userCommonProjection));
        when(passwordEncoder.encode(TestConstants.PASSWORD)).thenReturn(TestConstants.PASSWORD);
        assertEquals(UserSuccessMessage.PASSWORD_SUCCESSFULLY_CHANGED, authenticationService.passwordReset(
                TestConstants.USER_EMAIL, TestConstants.PASSWORD, TestConstants.PASSWORD, bindingResult));
        verify(userRepository, times(1)).getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class);
        verify(userRepository, times(1)).updatePassword(TestConstants.PASSWORD, userCommonProjection.getId());
        verify(userRepository, times(1)).updatePasswordResetCode(null, userCommonProjection.getId());
    }

    @Test
    public void passwordReset_ShouldThrowInputFieldException() {
        InputFieldException exception = assertThrows(InputFieldException.class,
                () -> authenticationService.passwordReset(TestConstants.USER_EMAIL, null, "test", bindingResult));
        assertEquals(Map.of("password", UserErrorMessage.PASSWORDS_NOT_MATCH), exception.getErrorsMap());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void passwordReset_ShouldThrowUserNotFoundInputFieldException() {
        when(userRepository.getUserByEmail(TestConstants.USER_EMAIL, UserCommonProjection.class))
                .thenReturn(Optional.empty());
        InputFieldException exception = assertThrows(InputFieldException.class,
                () -> authenticationService.passwordReset(TestConstants.USER_EMAIL, TestConstants.PASSWORD, TestConstants.PASSWORD, bindingResult));
        assertEquals(Map.of("email", UserErrorMessage.EMAIL_NOT_FOUND), exception.getErrorsMap());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void currentPasswordReset_ShouldReturnSuccessMessage() {
        when(userRepository.getUserPasswordById(TestConstants.USER_ID)).thenReturn(TestConstants.PASSWORD);
        when(passwordEncoder.matches(TestConstants.PASSWORD, TestConstants.PASSWORD)).thenReturn(true);
        when(passwordEncoder.encode(TestConstants.PASSWORD)).thenReturn(TestConstants.PASSWORD);
        assertEquals(UserSuccessMessage.PASSWORD_SUCCESSFULLY_UPDATED, authenticationService.currentPasswordReset(
                TestConstants.PASSWORD, TestConstants.PASSWORD, TestConstants.PASSWORD, bindingResult));
        verify(userRepository, times(1)).getUserPasswordById(TestConstants.USER_ID);
        verify(passwordEncoder, times(1)).matches(TestConstants.PASSWORD, TestConstants.PASSWORD);
        verify(userRepository, times(1)).updatePassword(TestConstants.PASSWORD, TestConstants.USER_ID);
        verify(passwordEncoder, times(1)).encode(TestConstants.PASSWORD);
    }

    @Test
    public void currentPasswordReset_ShouldThrowEmailNotFoundException() {
        when(userRepository.getUserPasswordById(TestConstants.USER_ID)).thenReturn("test");
        when(passwordEncoder.matches(TestConstants.PASSWORD, "test")).thenReturn(false);
        InputFieldException exception = assertThrows(InputFieldException.class,
                () -> authenticationService.currentPasswordReset(TestConstants.PASSWORD, TestConstants.PASSWORD, "test", bindingResult));
        assertEquals(Map.of("currentPassword", UserErrorMessage.INCORRECT_PASSWORD), exception.getErrorsMap());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void currentPasswordReset_ShouldThrowInputFieldException() {
        when(userRepository.getUserPasswordById(TestConstants.USER_ID)).thenReturn(TestConstants.PASSWORD);
        when(passwordEncoder.matches(TestConstants.PASSWORD, TestConstants.PASSWORD)).thenReturn(true);
        InputFieldException exception = assertThrows(InputFieldException.class,
                () -> authenticationService.currentPasswordReset(TestConstants.PASSWORD, "", "test", bindingResult));
        assertEquals(Map.of("password", UserErrorMessage.PASSWORDS_NOT_MATCH), exception.getErrorsMap());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}

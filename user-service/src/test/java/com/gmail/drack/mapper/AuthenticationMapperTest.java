package com.gmail.drack.mapper;

import com.gmail.drack.UserServiceTestHelper;
import com.gmail.drack.constants.UserSuccessMessage;
import com.gmail.drack.dto.request.AuthenticationRequest;
import com.gmail.drack.dto.request.CurrentPasswordResetRequest;
import com.gmail.drack.dto.request.PasswordResetRequest;
import com.gmail.drack.dto.response.AuthUserResponse;
import com.gmail.drack.repository.projection.AuthUserProjection;
import com.gmail.drack.service.AuthenticationService;
import com.gmail.drack.commons.util.TestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;

import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationMapperTest {

    @InjectMocks
    private AuthenticationMapper authenticationMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ModelMapper modelMapper;

    BindingResult bindingResult = mock(BindingResult.class);

    @Test
    public void login() {
        AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        Map<String, Object> credentials = Map.of(
                "user", authUserProjection,
                "token", TestConstants.AUTH_TOKEN);
        AuthUserResponse authUserResponse = new AuthUserResponse();
        when(authenticationService.login(request, bindingResult)).thenReturn(credentials);
        when(modelMapper.map(authUserProjection, AuthUserResponse.class)).thenReturn(authUserResponse);
        authenticationMapper.login(request, bindingResult);
        verify(authenticationService, times(1)).login(request, bindingResult);
    }

    @Test
    public void getUserByToken() {
        AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();
        Map<String, Object> credentials = Map.of(
                "user", authUserProjection,
                "token", TestConstants.AUTH_TOKEN);
        AuthUserResponse authUserResponse = new AuthUserResponse();
        when(authenticationService.getUserByToken()).thenReturn(credentials);
        when(modelMapper.map(authUserProjection, AuthUserResponse.class)).thenReturn(authUserResponse);
        authenticationMapper.getUserByToken();
        verify(authenticationService, times(1)).getUserByToken();
    }

    @Test
    public void getExistingEmail() {
        when(authenticationService.getExistingEmail(TestConstants.USER_EMAIL, bindingResult))
                .thenReturn(UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND);
        authenticationMapper.getExistingEmail(TestConstants.USER_EMAIL, bindingResult);
        verify(authenticationService, times(1)).getExistingEmail(TestConstants.USER_EMAIL, bindingResult);
    }

    @Test
    public void sendPasswordResetCode() {
        when(authenticationService.sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult))
                .thenReturn(UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND);
        authenticationMapper.sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult);
        verify(authenticationService, times(1)).sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult);
    }

    @Test
    public void getUserByPasswordResetCode() {
        AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();
        AuthUserResponse authUserResponse = new AuthUserResponse();
        when(authenticationService.getUserByPasswordResetCode(TestConstants.ACTIVATION_CODE)).thenReturn(authUserProjection);
        when(modelMapper.map(authUserProjection, AuthUserResponse.class)).thenReturn(authUserResponse);
        authenticationMapper.getUserByPasswordResetCode(TestConstants.ACTIVATION_CODE);
        verify(authenticationService, times(1)).getUserByPasswordResetCode(TestConstants.ACTIVATION_CODE);
    }

    @Test
    public void passwordReset() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        request.setPassword(TestConstants.PASSWORD);
        request.setPassword2(TestConstants.PASSWORD);
        when(authenticationService.passwordReset(
                TestConstants.USER_EMAIL,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        )).thenReturn(UserSuccessMessage.PASSWORD_SUCCESSFULLY_CHANGED);
        authenticationMapper.passwordReset(request, bindingResult);
        verify(authenticationService, times(1)).passwordReset(
                TestConstants.USER_EMAIL,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        );
    }

    @Test
    public void currentPasswordReset() {
        CurrentPasswordResetRequest request = new CurrentPasswordResetRequest();
        request.setCurrentPassword(TestConstants.PASSWORD);
        request.setPassword(TestConstants.PASSWORD);
        request.setPassword2(TestConstants.PASSWORD);
        when(authenticationService.currentPasswordReset(
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        )).thenReturn(UserSuccessMessage.PASSWORD_SUCCESSFULLY_UPDATED);
        authenticationMapper.currentPasswordReset(request, bindingResult);
        verify(authenticationService, times(1)).currentPasswordReset(
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        );
    }
}

package com.gmail.drack.service.impl;

import com.gmail.drack.commons.constants.HeaderConstants;
import com.gmail.drack.commons.exception.InputFieldException;
import com.gmail.drack.constants.UserErrorMessage;
import com.gmail.drack.constants.UserSuccessMessage;
import com.gmail.drack.dto.request.AuthenticationRequest;
import com.gmail.drack.commons.event.SendEmailEvent;
import com.gmail.drack.commons.exception.ApiRequestException;
import com.gmail.drack.broker.producer.SendEmailProducer;
import com.gmail.drack.model.User;
import com.gmail.drack.model.UserRole;
import com.gmail.drack.repository.UserRepository;
import com.gmail.drack.repository.projection.AuthUserProjection;
import com.gmail.drack.repository.projection.UserCommonProjection;
import com.gmail.drack.repository.projection.UserPrincipalProjection;
import com.gmail.drack.commons.security.JwtProvider;
import com.gmail.drack.service.AuthenticationService;
import com.gmail.drack.service.util.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

import static com.gmail.drack.broker.producer.SendEmailProducer.toSendPasswordResetEmailEvent;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserServiceHelper userServiceHelper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final SendEmailProducer sendEmailProducer;

    @Override
    public Long getAuthenticatedUserId() {
        return getUserId();
    }

    @Override
    public User getAuthenticatedUser() {
        return userRepository.findById(getUserId())
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public UserPrincipalProjection getUserPrincipalByEmail(String email) {
        return userRepository.getUserByEmail(email, UserPrincipalProjection.class)
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Map<String, Object> login(AuthenticationRequest request, BindingResult bindingResult) {
        userServiceHelper.processInputErrors(bindingResult);
        AuthUserProjection user = userRepository.getUserByEmail(request.getEmail(), AuthUserProjection.class)
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        String token = jwtProvider.createToken(request.getEmail(), UserRole.USER.name());
        return Map.of("user", user, "token", token);
    }

    @Override
    public Map<String, Object> getUserByToken() {
        AuthUserProjection user = userRepository.getUserById(getUserId(), AuthUserProjection.class)
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        String token = jwtProvider.createToken(user.getEmail(), UserRole.USER.name());
        return Map.of("user", user, "token", token);
    }

    @Override
    public String getExistingEmail(String email, BindingResult bindingResult) {
        userServiceHelper.processInputErrors(bindingResult);
        userRepository.getUserByEmail(email, UserCommonProjection.class)
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND));
        return UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND;
    }

    @Override
    @Transactional
    public String sendPasswordResetCode(String email, BindingResult bindingResult) {
        userServiceHelper.processInputErrors(bindingResult);
        UserCommonProjection user = userRepository.getUserByEmail(email, UserCommonProjection.class)
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND));
        userRepository.updatePasswordResetCode(UUID.randomUUID().toString().substring(0, 7), user.getId());
        String passwordResetCode = userRepository.getPasswordResetCode(user.getId());
        SendEmailEvent sendEmailEvent = toSendPasswordResetEmailEvent(user.getEmail(), user.getFullName(), passwordResetCode);
        sendEmailProducer.sendEmail(sendEmailEvent);
        return UserSuccessMessage.RESET_PASSWORD_CODE_IS_SEND;
    }

    @Override
    public AuthUserProjection getUserByPasswordResetCode(String code) {
        return userRepository.getByPasswordResetCode(code)
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.INVALID_PASSWORD_RESET_CODE, HttpStatus.BAD_REQUEST));
    }

    @Override
    @Transactional
    public String passwordReset(String email, String password, String password2, BindingResult bindingResult) {
        userServiceHelper.processInputErrors(bindingResult);
        checkMatchPasswords(password, password2);
        UserCommonProjection user = userRepository.getUserByEmail(email, UserCommonProjection.class)
                .orElseThrow(() -> new InputFieldException(HttpStatus.NOT_FOUND, Map.of("email", UserErrorMessage.EMAIL_NOT_FOUND)));
        userRepository.updatePassword(passwordEncoder.encode(password), user.getId());
        userRepository.updatePasswordResetCode(null, user.getId());
        return UserSuccessMessage.PASSWORD_SUCCESSFULLY_CHANGED;
    }

    @Override
    @Transactional
    public String currentPasswordReset(String currentPassword, String password, String password2, BindingResult bindingResult) {
        userServiceHelper.processInputErrors(bindingResult);
        Long authUserId = getAuthenticatedUserId();
        String userPassword = userRepository.getUserPasswordById(authUserId);

        if (!passwordEncoder.matches(currentPassword, userPassword)) {
            processPasswordException("currentPassword", UserErrorMessage.INCORRECT_PASSWORD, HttpStatus.NOT_FOUND);
        }
        checkMatchPasswords(password, password2);
        userRepository.updatePassword(passwordEncoder.encode(password), authUserId);
        return UserSuccessMessage.PASSWORD_SUCCESSFULLY_UPDATED;
    }

    private Long getUserId() {
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) attribs).getRequest();
        return Long.parseLong(request.getHeader(HeaderConstants.AUTH_USER_ID_HEADER));
    }

    private void checkMatchPasswords(String password, String password2) {
        if (password == null || !password.equals(password2)) {
            processPasswordException("password", UserErrorMessage.PASSWORDS_NOT_MATCH, HttpStatus.BAD_REQUEST);
        }
    }

    private void processPasswordException(String paramName, String exceptionMessage, HttpStatus status) {
        throw new InputFieldException(status, Map.of(paramName, exceptionMessage));
    }
}

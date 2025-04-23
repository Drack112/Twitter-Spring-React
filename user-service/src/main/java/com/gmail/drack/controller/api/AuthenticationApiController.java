package com.gmail.drack.controller.api;

import com.gmail.drack.commons.constants.PathConstants;
import com.gmail.drack.commons.dto.response.user.UserPrincipalResponse;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_AUTH)
public class AuthenticationApiController {

    private final AuthenticationService authenticationService;
    private final BasicMapper mapper;

    @GetMapping(PathConstants.USER_EMAIL)
    public UserPrincipalResponse getUserPrincipalByEmail(@PathVariable("email") String email) {
        return mapper.convertToResponse(authenticationService.getUserPrincipalByEmail(email), UserPrincipalResponse.class);
    }
}

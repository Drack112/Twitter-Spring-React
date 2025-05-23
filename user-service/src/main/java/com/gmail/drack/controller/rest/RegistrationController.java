package com.gmail.drack.controller.rest;

import com.gmail.drack.commons.constants.PathConstants;
import com.gmail.drack.dto.request.EndRegistrationRequest;
import com.gmail.drack.dto.request.ProcessEmailRequest;
import com.gmail.drack.dto.request.RegistrationRequest;
import com.gmail.drack.dto.response.AuthenticationResponse;
import com.gmail.drack.mapper.RegistrationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_AUTH)
public class RegistrationController {

    private final RegistrationMapper registrationMapper;

    @PostMapping(PathConstants.REGISTRATION_CHECK)
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationRequest request, BindingResult bindingResult) {
        return ResponseEntity.ok(registrationMapper.registration(request, bindingResult));
    }

    @PostMapping(PathConstants.REGISTRATION_CODE)
    public ResponseEntity<String> sendRegistrationCode(@Valid @RequestBody ProcessEmailRequest request, BindingResult bindingResult) {
        return ResponseEntity.ok(registrationMapper.sendRegistrationCode(request.getEmail(), bindingResult));
    }

    @GetMapping(PathConstants.REGISTRATION_ACTIVATE_CODE)
    public ResponseEntity<String> checkRegistrationCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(registrationMapper.checkRegistrationCode(code));
    }

    @PostMapping(PathConstants.REGISTRATION_CONFIRM)
    public ResponseEntity<AuthenticationResponse> endRegistration(@Valid @RequestBody EndRegistrationRequest request, BindingResult bindingResult) {
        return ResponseEntity.ok(registrationMapper.endRegistration(request, bindingResult));
    }
}

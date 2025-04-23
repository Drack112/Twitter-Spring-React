package com.gmail.drack.service.impl;

import com.gmail.drack.broker.producer.MuteUserProducer;
import com.gmail.drack.model.User;
import com.gmail.drack.repository.MuteUserRepository;
import com.gmail.drack.repository.projection.MutedUserProjection;
import com.gmail.drack.service.AuthenticationService;
import com.gmail.drack.service.MuteUserService;
import com.gmail.drack.service.util.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MuteUserServiceImpl implements MuteUserService {

    private final MuteUserRepository muteUserRepository;
    private final AuthenticationService authenticationService;
    private final UserServiceHelper userServiceHelper;
    private final MuteUserProducer muteUserProducer;

    @Override
    public Page<MutedUserProjection> getMutedList(Pageable pageable) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return muteUserRepository.getUserMuteListById(authUserId, pageable);
    }

    @Override
    @Transactional
    public Boolean processMutedList(Long userId) {
        User user = userServiceHelper.getUserById(userId);
        Long authUserId = authenticationService.getAuthenticatedUserId();
        boolean hasUserMuted;

        if (muteUserRepository.isUserMuted(authUserId, userId)) {
            muteUserRepository.unmuteUser(authUserId, userId);
            hasUserMuted = false;
        } else {
            muteUserRepository.muteUser(authUserId, userId);
            hasUserMuted = true;
        }
        muteUserProducer.sendMuteUserEvent(user, authUserId, hasUserMuted);
        return hasUserMuted;
    }
}

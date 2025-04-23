package com.gmail.drack.service.impl;

import com.gmail.drack.commons.dto.response.notification.NotificationUserResponse;
import com.gmail.drack.commons.dto.response.user.UserResponse;
import com.gmail.drack.commons.event.UpdateUserEvent;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.model.User;
import com.gmail.drack.repository.UserRepository;
import com.gmail.drack.repository.projection.NotificationUserProjection;
import com.gmail.drack.repository.projection.UserProjection;
import com.gmail.drack.service.UserClientService;
import com.gmail.drack.commons.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserRepository userRepository;
    private final BasicMapper basicMapper;

    @Override
    public UserResponse getUserResponseById(Long userId) {
        UserProjection user = userRepository.getUserById(userId, UserProjection.class).get();
        return basicMapper.convertToResponse(user, UserResponse.class);
    }

    @Override
    public List<NotificationUserResponse> getUsersWhichUserSubscribed() {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        List<NotificationUserProjection> users = userRepository.getUsersWhichUserSubscribed(authUserId);
        return basicMapper.convertToResponseList(users, NotificationUserResponse.class);
    }

    @Override
    public List<Long> getUserIdsWhichUserSubscribed() {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.getUserIdsWhichUserSubscribed(authUserId);
    }

    @Override
    public List<UpdateUserEvent> getBatchUsers(Integer period, Integer page, Integer limit) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(period);
        PageRequest pageable = PageRequest.of(page, limit);
        List<User> users = userRepository.findByRegistrationAndUpdatedDate(sinceDate, pageable);
        return basicMapper.convertToResponseList(users, UpdateUserEvent.class);
    }
}

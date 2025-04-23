package com.gmail.drack.service;

import java.util.Optional;

import com.gmail.drack.models.User;

public interface UserService {
    User getAuthUser();

    Optional<User> getUserById(Long userId);

    Optional<User> getUserIdByUsername(String username);

    boolean isUserHavePrivateProfile(Long userId);

    boolean isMyProfileBlockedByUser(Long userId);
}

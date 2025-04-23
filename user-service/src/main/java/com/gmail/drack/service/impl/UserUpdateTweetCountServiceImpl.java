package com.gmail.drack.service.impl;

import com.gmail.drack.commons.event.UpdateTweetCountEvent;
import com.gmail.drack.commons.exception.ApiRequestException;
import com.gmail.drack.constants.UserErrorMessage;
import com.gmail.drack.model.User;
import com.gmail.drack.repository.UserRepository;
import com.gmail.drack.service.UserUpdateTweetCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUpdateTweetCountServiceImpl implements UserUpdateTweetCountService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void handleUpdateTweetCount(UpdateTweetCountEvent tweetCountEvent, String authId) {
        User user = getUserById(Long.parseLong(authId));
        userRepository.updateTweetCount(tweetCountEvent.isUpdateTweetsCount(), user.getId());
    }

    @Override
    @Transactional
    public void handleUpdateLikeTweetCount(UpdateTweetCountEvent tweetCountEvent, String authId) {
        User user = getUserById(Long.parseLong(authId));
        userRepository.updateLikeCount(tweetCountEvent.isUpdateTweetsCount(), user.getId());
    }

    @Override
    @Transactional
    public void handleUpdateMediaTweetCount(UpdateTweetCountEvent tweetCountEvent, String authId) {
        User user = getUserById(Long.parseLong(authId));
        userRepository.updateMediaTweetCount(tweetCountEvent.isUpdateTweetsCount(), user.getId());
    }

    private User getUserById(Long userId) {
        return userRepository.getUserById(userId, User.class)
                .orElseThrow(() -> new ApiRequestException(UserErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}

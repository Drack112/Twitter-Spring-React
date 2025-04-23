package com.gmail.drack.service.util;

import org.flywaydb.core.api.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.gmail.drack.commons.constants.ErrorMessage;
import com.gmail.drack.commons.exception.ApiRequestException;
import com.gmail.drack.commons.util.AuthUtil;
import com.gmail.drack.constants.TweetErrorMessage;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.models.User;
import com.gmail.drack.repository.TweetRepository;
import com.gmail.drack.repository.UserRepository;
import com.gmail.drack.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TweetValidationHelper {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final UserService userService;

    public Tweet checkValidTweet(Long tweetId) {
        Tweet tweet = tweetRepository.getTweetById(tweetId, Tweet.class)
                .orElseThrow(() -> new ApiRequestException(TweetErrorMessage.TWEET_NOT_FOUND, HttpStatus.NOT_FOUND));
        validateTweet(tweet.isDeleted(), tweet.getAuthor().getId());
        return tweet;
    }

    public void validateTweet(boolean isDeleted, Long tweetAuthorId) {
        if (isDeleted) {
            throw new ApiRequestException(TweetErrorMessage.TWEET_DELETED, HttpStatus.BAD_REQUEST);
        }
        checkIsValidUserProfile(tweetAuthorId);
    }

    public User validateUserProfile(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ApiRequestException(String.format(ErrorMessage.USER_NOT_FOUND, userId),
                        HttpStatus.NOT_FOUND));
        checkIsValidUserProfile(user.getId());
        return user;
    }

    public void checkIsValidUserProfile(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();

        if (!userId.equals(authUserId)) {
            if (userService.isUserHavePrivateProfile(authUserId)) {
                throw new ApiRequestException(ErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (userService.isMyProfileBlockedByUser(userId)) {
                throw new ApiRequestException(ErrorMessage.USER_PROFILE_BLOCKED, HttpStatus.BAD_REQUEST);
            }
        }
    }

    public void checkTweetTextLength(String text) {
        if (text.length() == 0 || text.length() > 280) {
            throw new ApiRequestException(TweetErrorMessage.INCORRECT_TWEET_TEXT_LENGTH, HttpStatus.BAD_REQUEST);
        }
    }
}

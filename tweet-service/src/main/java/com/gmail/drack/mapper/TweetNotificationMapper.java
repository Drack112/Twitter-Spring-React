package com.gmail.drack.mapper;

import org.springframework.stereotype.Component;

import com.gmail.drack.commons.dto.response.tweet.TweetResponse;
import com.gmail.drack.commons.enums.NotificationType;
import com.gmail.drack.commons.event.TweetMentionNotificationEvent;
import com.gmail.drack.commons.event.TweetNotificationDto;
import com.gmail.drack.commons.event.TweetNotificationEvent;
import com.gmail.drack.commons.event.TweetSubscriberNotificationEvent;
import com.gmail.drack.commons.event.UserNotificationDto;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.models.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TweetNotificationMapper {
    private final BasicMapper basicMapper;

    public TweetNotificationEvent toTweetNotificationEvent(NotificationType type, Tweet tweet, User authUser,
            boolean condition) {
        return TweetNotificationEvent.builder()
                .notificationType(type)
                .notificationCondition(condition)
                .notifiedUser(toUserDto(tweet.getAuthor()))
                .user(toUserDto(authUser))
                .tweet(toTweetDto(tweet))
                .build();
    }

    public TweetSubscriberNotificationEvent toTweetSubscriberNotificationEvent(Tweet tweet, User authUser) {
        return TweetSubscriberNotificationEvent.builder()
                .tweet(toTweetDto(tweet))
                .user(toUserDto(authUser))
                .build();
    }

    public TweetMentionNotificationEvent toTweetMentionNotificationEvent(NotificationType notificationType, Tweet tweet,
            User user, User authUser) {
        return TweetMentionNotificationEvent.builder()
                .notificationType(notificationType)
                .notifiedUser(toUserDto(user))
                .user(toUserDto(authUser))
                .tweet(toTweetDto(tweet))
                .tweetResponse(toTweetResponse(tweet))
                .build();
    }

    private UserNotificationDto toUserDto(User user) {
        return basicMapper.convertToResponse(user, UserNotificationDto.class);
    }

    private TweetNotificationDto toTweetDto(Tweet tweet) {
        return basicMapper.convertToResponse(tweet, TweetNotificationDto.class);
    }

    private TweetResponse toTweetResponse(Tweet tweet) {
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }
}

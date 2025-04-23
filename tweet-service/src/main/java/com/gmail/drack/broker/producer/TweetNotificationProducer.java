package com.gmail.drack.broker.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.enums.NotificationType;
import com.gmail.drack.commons.event.TweetMentionNotificationEvent;
import com.gmail.drack.commons.event.TweetNotificationEvent;
import com.gmail.drack.commons.event.TweetSubscriberNotificationEvent;
import com.gmail.drack.mapper.TweetNotificationMapper;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.models.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TweetNotificationProducer {
    private final KafkaTemplate<String, TweetNotificationEvent> tweetNotificationTemplate;
    private final KafkaTemplate<String, TweetSubscriberNotificationEvent> tweetSubscriberNotificationTemplate;
    private final KafkaTemplate<String, TweetMentionNotificationEvent> tweetMentionNotificationTemplate;
    private final TweetNotificationMapper mapper;

    public void sendTweetNotificationEvent(NotificationType type, Tweet tweet, User authUser, boolean condition) {
        TweetNotificationEvent event = mapper.toTweetNotificationEvent(type, tweet, authUser, condition);
        tweetNotificationTemplate.send(KafkaTopicConstants.SEND_TWEET_NOTIFICATION_TOPIC, event);
    }

    public void sendTweetSubscriberNotificationEvent(Tweet tweet, User authUser) {
        TweetSubscriberNotificationEvent event = mapper.toTweetSubscriberNotificationEvent(tweet, authUser);
        tweetSubscriberNotificationTemplate.send(KafkaTopicConstants.SEND_SUBSCRIBER_NOTIFICATION_TOPIC, event);
    }

    public void sendTweetMentionNotificationEvent(NotificationType notificationType, Tweet tweet, User user,
            User authUser) {
        TweetMentionNotificationEvent event = mapper.toTweetMentionNotificationEvent(notificationType, tweet, user,
                authUser);
        tweetMentionNotificationTemplate.send(KafkaTopicConstants.SEND_TWEET_MENTION_NOTIFICATION_TOPIC, event);
    }
}

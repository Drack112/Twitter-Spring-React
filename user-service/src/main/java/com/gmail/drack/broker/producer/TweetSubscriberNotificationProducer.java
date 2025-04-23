package com.gmail.drack.broker.producer;

import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.TweetSubscriberNotificationEvent;
import com.gmail.drack.mapper.ProducerMapper;
import com.gmail.drack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TweetSubscriberNotificationProducer {

    private final KafkaTemplate<String, TweetSubscriberNotificationEvent> kafkaTemplate;
    private final ProducerMapper producerMapper;

    public void sendTweetSubscriberNotificationEvent(TweetSubscriberNotificationEvent notificationEvent, List<User> subscribers) {
        TweetSubscriberNotificationEvent event = producerMapper.toTweetSubscriberNotificationEvent(notificationEvent, subscribers);
        kafkaTemplate.send(KafkaTopicConstants.SEND_TWEET_SUBSCRIBER_NOTIFICATION_TOPIC, event);
    }
}

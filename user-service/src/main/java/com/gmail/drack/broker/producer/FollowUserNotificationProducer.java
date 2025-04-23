package com.gmail.drack.broker.producer;

import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.FollowUserNotificationEvent;
import com.gmail.drack.mapper.ProducerMapper;
import com.gmail.drack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowUserNotificationProducer {

    private final KafkaTemplate<String, FollowUserNotificationEvent> kafkaTemplate;
    private final ProducerMapper producerMapper;

    public void sendFollowUserNotificationEvent(User authUser, User notifiedUser) {
        FollowUserNotificationEvent event = producerMapper.toUserNotificationEvent(authUser, notifiedUser);
        kafkaTemplate.send(KafkaTopicConstants.SEND_USER_NOTIFICATION_TOPIC, event);
    }
}

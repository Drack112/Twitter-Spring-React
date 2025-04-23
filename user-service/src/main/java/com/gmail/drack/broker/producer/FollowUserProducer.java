package com.gmail.drack.broker.producer;

import com.gmail.drack.broker.util.ProducerUtil;
import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.FollowUserEvent;
import com.gmail.drack.mapper.ProducerMapper;
import com.gmail.drack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowUserProducer {

    private final KafkaTemplate<String, FollowUserEvent> kafkaTemplate;
    private final ProducerMapper producerMapper;

    public void sendFollowUserEvent(User user, Long authUserId, boolean hasUserFollowed) {
        FollowUserEvent event = producerMapper.toFollowUserEvent(user, hasUserFollowed);
        kafkaTemplate.send(ProducerUtil.authHeaderWrapper(KafkaTopicConstants.FOLLOW_USER_TOPIC, event, authUserId));
    }
}

package com.gmail.drack.broker.producer;

import com.gmail.drack.broker.util.ProducerUtil;
import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.FollowRequestUserEvent;
import com.gmail.drack.mapper.ProducerMapper;
import com.gmail.drack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowRequestUserProducer {

    private final KafkaTemplate<String, FollowRequestUserEvent> kafkaTemplate;
    private final ProducerMapper producerMapper;

    public void sendFollowRequestUserEvent(User user, Long authUserId, boolean hasUserFollowRequest) {
        FollowRequestUserEvent event = producerMapper.toFollowRequestUserEvent(user, hasUserFollowRequest);
        kafkaTemplate.send(ProducerUtil.authHeaderWrapper(KafkaTopicConstants.FOLLOW_REQUEST_USER_TOPIC, event, authUserId));
    }
}

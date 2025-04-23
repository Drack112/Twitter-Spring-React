package com.gmail.drack.broker.producer;

import com.gmail.drack.broker.util.ProducerUtil;
import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.MuteUserEvent;
import com.gmail.drack.mapper.ProducerMapper;
import com.gmail.drack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MuteUserProducer {

    private final KafkaTemplate<String, MuteUserEvent> kafkaTemplate;
    private final ProducerMapper producerMapper;

    public void sendMuteUserEvent(User user, Long authUserId, boolean hasUserMuted) {
        MuteUserEvent event = producerMapper.toMuteUserEvent(user, hasUserMuted);
        kafkaTemplate.send(ProducerUtil.authHeaderWrapper(KafkaTopicConstants.MUTE_USER_TOPIC, event, authUserId));
    }
}

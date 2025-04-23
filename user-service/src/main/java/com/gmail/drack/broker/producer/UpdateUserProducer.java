package com.gmail.drack.broker.producer;

import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.UpdateUserEvent;
import com.gmail.drack.mapper.ProducerMapper;
import com.gmail.drack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserProducer {

    private final KafkaTemplate<String, UpdateUserEvent> kafkaTemplate;
    private final ProducerMapper producerMapper;

    public void sendUpdateUserEvent(User user) {
        UpdateUserEvent updateUserEvent = producerMapper.toUpdateUserEvent(user);
        kafkaTemplate.send(KafkaTopicConstants.UPDATE_USER_TOPIC, updateUserEvent);
    }
}

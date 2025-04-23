package com.gmail.drack.broker.producer;

import com.gmail.drack.broker.util.ProducerUtil;
import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.BlockUserEvent;
import com.gmail.drack.mapper.ProducerMapper;
import com.gmail.drack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockUserProducer {

    private final KafkaTemplate<String, BlockUserEvent> kafkaTemplate;
    private final ProducerMapper producerMapper;

    public void sendBlockUserEvent(User user, Long authUserId, boolean hasUserBlocked) {
        BlockUserEvent event = producerMapper.toBlockUserEvent(user, hasUserBlocked);
        kafkaTemplate.send(ProducerUtil.authHeaderWrapper(KafkaTopicConstants.BLOCK_USER_TOPIC, event, authUserId));
    }
}

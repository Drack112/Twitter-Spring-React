package com.gmail.drack.broker.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.gmail.drack.commons.constants.KafkaTopicConstants;
import com.gmail.drack.commons.event.TweetTagEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagProducer {
    private final KafkaTemplate<String, TweetTagEvent> parseHashtagKafkaTemplate;
    private final KafkaTemplate<String, Long> deleteTagKafkaTemplate;

    public void parseHashtag(Long tweetId, String tweetText) {
        parseHashtagKafkaTemplate.send(KafkaTopicConstants.PARSE_TWEETS_HASHTAG_TOPIC,
                new TweetTagEvent(tweetId, tweetText));
    }

    public void deleteTag(Long tweetId) {
        deleteTagKafkaTemplate.send(KafkaTopicConstants.DELETE_TWEETS_HASHTAG_TOPIC, tweetId);
    }
}

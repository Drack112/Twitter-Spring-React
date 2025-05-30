package com.gmail.drack.service.impl;

import com.gmail.drack.broker.producer.TweetSubscriberNotificationProducer;
import com.gmail.drack.commons.event.TweetSubscriberNotificationEvent;
import com.gmail.drack.model.User;
import com.gmail.drack.repository.UserRepository;
import com.gmail.drack.service.UserNotificationHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationHandlerServiceImpl implements UserNotificationHandlerService {

    private final UserRepository userRepository;
    private final TweetSubscriberNotificationProducer tweetSubscriberNotificationProducer;

    @Override
    @Transactional
    public void increaseNotificationsCount(Long notifiedUserEventId) {
        userRepository.increaseNotificationsCount(notifiedUserEventId);
    }

    @Override
    @Transactional
    public void increaseMentionsCount(Long notifiedUserEventId) {
        userRepository.increaseMentionsCount(notifiedUserEventId);
    }

    @Override
    @Transactional
    public void resetNotificationCount(Long notifiedUserEventId) {
        userRepository.resetNotificationCount(notifiedUserEventId);
    }

    @Override
    @Transactional
    public void resetMentionCount(Long notifiedUserEventId) {
        userRepository.resetMentionCount(notifiedUserEventId);
    }

    @Override
    public void processSubscriberNotificationListener(TweetSubscriberNotificationEvent event) {
        List<User> subscribers = userRepository.getSubscribersByUserId(event.getUser().getId());
        tweetSubscriberNotificationProducer.sendTweetSubscriberNotificationEvent(event, subscribers);
    }
}

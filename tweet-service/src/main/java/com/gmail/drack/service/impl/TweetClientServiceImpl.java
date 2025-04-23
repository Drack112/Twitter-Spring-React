package com.gmail.drack.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gmail.drack.commons.dto.request.IdsRequest;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.repository.TweetRepository;
import com.gmail.drack.repository.projection.ChatTweetProjection;
import com.gmail.drack.repository.projection.TweetProjection;
import com.gmail.drack.service.TweetClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetClientServiceImpl implements TweetClientService {
    private final TweetRepository tweetRepository;

    @Override
    public List<TweetProjection> getTweetsByIds(IdsRequest requests) {
        return tweetRepository.getTweetListsByIds(requests.getIds());
    }

    @Override
    public Page<TweetProjection> getTweetsByUserIds(IdsRequest request, Pageable pageable) {
        return tweetRepository.getTweetsByAuthorIds(request.getIds(), pageable);
    }

    @Override
    public TweetProjection getTweetById(Long tweetId) {
        return tweetRepository.getTweetById(tweetId, TweetProjection.class).get();
    }

    @Override
    public Boolean isTweetExists(Long tweetId) {
        return tweetRepository.isTweetExists(tweetId);
    }

    @Override
    public Long getTweetCountByText(String text) {
        return tweetRepository.getTweetCountByText(text);
    }

    @Override
    public ChatTweetProjection getChatTweet(Long tweetId) {
        return tweetRepository.getTweetById(tweetId, ChatTweetProjection.class).get();
    }

    @Override
    public List<Tweet> getBatchTweets(Integer period, Integer page, Integer limit) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(period);
        PageRequest pageable = PageRequest.of(page, limit);
        return tweetRepository.findByCreationAndUpdatedDate(sinceDate, pageable);
    }
}

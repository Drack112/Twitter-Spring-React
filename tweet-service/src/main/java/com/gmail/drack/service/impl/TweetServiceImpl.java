package com.gmail.drack.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.drack.commons.exception.ApiRequestException;
import com.gmail.drack.constants.TweetErrorMessage;
import com.gmail.drack.repository.TweetRepository;
import com.gmail.drack.repository.projection.TweetProjection;
import com.gmail.drack.service.TweetService;
import com.gmail.drack.service.util.TweetValidationHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final TweetValidationHelper tweetValidationHelper;

    @Override
    @Transactional(readOnly = true)
    public TweetProjection getTweetById(Long tweetId) {
        TweetProjection tweet = tweetRepository.getTweetById(tweetId, TweetProjection.class)
                .orElseThrow(() -> new ApiRequestException(TweetErrorMessage.TWEET_NOT_FOUND, HttpStatus.NOT_FOUND));
        tweetValidationHelper.validateTweet(tweet.isDeleted(), tweet.getAuthor().getId());
        return tweet;
    }
}

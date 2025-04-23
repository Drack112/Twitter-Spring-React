package com.gmail.drack.service;

import com.gmail.drack.repository.projection.TweetProjection;

public interface TweetService {
    TweetProjection getTweetById(Long tweetId);
}

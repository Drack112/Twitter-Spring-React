package com.gmail.drack.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gmail.drack.commons.dto.request.IdsRequest;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.repository.projection.ChatTweetProjection;
import com.gmail.drack.repository.projection.TweetProjection;

public interface TweetClientService {

    List<TweetProjection> getTweetsByIds(IdsRequest requests);

    Page<TweetProjection> getTweetsByUserIds(IdsRequest requests, Pageable pageable);

    TweetProjection getTweetById(Long tweetId);

    Boolean isTweetExists(Long tweetId);

    Long getTweetCountByText(String text);

    ChatTweetProjection getChatTweet(Long tweetId);

    List<Tweet> getBatchTweets(Integer period, Integer page, Integer limit);

}

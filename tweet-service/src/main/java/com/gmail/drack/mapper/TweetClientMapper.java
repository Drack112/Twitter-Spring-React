package com.gmail.drack.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.gmail.drack.commons.dto.HeaderResponse;
import com.gmail.drack.commons.dto.request.IdsRequest;
import com.gmail.drack.commons.dto.response.chat.ChatTweetResponse;
import com.gmail.drack.commons.dto.response.tweet.TweetResponse;
import com.gmail.drack.commons.event.UpdateTweetEvent;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.repository.projection.ChatTweetProjection;
import com.gmail.drack.repository.projection.TweetProjection;
import com.gmail.drack.service.TweetClientService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TweetClientMapper {

    private final BasicMapper basicMapper;
    private final TweetClientService tweetClientService;

    public List<TweetResponse> getTweetsByIds(IdsRequest requests) {
        List<TweetProjection> tweets = tweetClientService.getTweetsByIds(requests);
        return basicMapper.convertToResponseList(tweets, TweetResponse.class);
    }

    public HeaderResponse<TweetResponse> getTweetsByUserIds(IdsRequest requests, Pageable pageable) {
        Page<TweetProjection> tweets = tweetClientService.getTweetsByUserIds(requests, pageable);
        return basicMapper.getHeaderResponse(tweets, TweetResponse.class);
    }

    public TweetResponse getTweetById(Long tweetId) {
        TweetProjection tweet = tweetClientService.getTweetById(tweetId);
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public Boolean isTweetExists(Long tweetId) {
        return tweetClientService.isTweetExists(tweetId);
    }

    public Long getTweetCountByText(String text) {
        return tweetClientService.getTweetCountByText(text);
    }

    public ChatTweetResponse getChatTweet(Long tweetId) {
        ChatTweetProjection chatTweet = tweetClientService.getChatTweet(tweetId);
        return basicMapper.convertToResponse(chatTweet, ChatTweetResponse.class);
    }

    public List<UpdateTweetEvent> getBatchTweets(Integer period, Integer page, Integer limit) {
        List<Tweet> tweets = tweetClientService.getBatchTweets(period, page, limit);
        return basicMapper.convertToResponseList(tweets, UpdateTweetEvent.class);
    }
}

package com.gmail.drack.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.drack.client.WebSocketClient;
import com.gmail.drack.commons.constants.PathConstants;
import com.gmail.drack.commons.constants.WebsocketConstants;
import com.gmail.drack.commons.dto.response.tweet.TweetResponse;
import com.gmail.drack.dtos.request.TweetRequest;
import com.gmail.drack.dtos.request.VoteRequest;
import com.gmail.drack.mapper.PollMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_TWEETS)
public class PollController {
    private final PollMapper pollMapper;
    private final WebSocketClient webSocketClient;

    @PostMapping(PathConstants.POLL)
    public ResponseEntity<TweetResponse> createPoll(@RequestBody TweetRequest tweetRequest) {
        TweetResponse tweet = pollMapper.createPoll(tweetRequest);
        webSocketClient.send(WebsocketConstants.TOPIC_FEED_ADD, tweet);
        webSocketClient.send(WebsocketConstants.TOPIC_USER_ADD_TWEET + tweet.getAuthor().getId(), tweet);
        return ResponseEntity.ok(tweet);
    }

    @PostMapping(PathConstants.VOTE)
    public ResponseEntity<TweetResponse> voteInPoll(@RequestBody VoteRequest voteRequest) {
        TweetResponse tweet = pollMapper.voteInPoll(voteRequest);
        webSocketClient.send(WebsocketConstants.TOPIC_FEED_VOTE, tweet);
        webSocketClient.send(WebsocketConstants.TOPIC_TWEET_VOTE + tweet.getId(), tweet);
        webSocketClient.send(WebsocketConstants.TOPIC_USER_VOTE_TWEET + tweet.getAuthor().getId(), tweet);
        return ResponseEntity.ok(tweet);
    }

}

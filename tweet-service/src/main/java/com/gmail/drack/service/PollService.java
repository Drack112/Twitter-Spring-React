package com.gmail.drack.service;

import java.util.List;

import com.gmail.drack.commons.dto.response.tweet.TweetResponse;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.repository.projection.TweetProjection;

public interface PollService {
    TweetResponse createPoll(Long pollDateTime, List<String> choices, Tweet tweet);

    TweetProjection voteInPoll(Long tweetId, Long pollId, Long pollChoiceId);
}

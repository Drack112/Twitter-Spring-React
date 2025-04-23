package com.gmail.drack.mapper;

import org.springframework.stereotype.Component;

import com.gmail.drack.commons.dto.response.tweet.TweetResponse;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.dtos.request.TweetRequest;
import com.gmail.drack.dtos.request.VoteRequest;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.repository.projection.TweetProjection;
import com.gmail.drack.service.PollService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PollMapper {
    private final BasicMapper basicMapper;
    private final PollService pollService;

    public TweetResponse createPoll(TweetRequest tweetRequest) {
        Tweet tweet = basicMapper.convertToResponse(tweetRequest, Tweet.class);
        return pollService.createPoll(tweetRequest.getPollDateTime(), tweetRequest.getChoices(), tweet);
    }

    public TweetResponse voteInPoll(VoteRequest voteRequest) {
        TweetProjection tweet = pollService.voteInPoll(voteRequest.getTweetId(), voteRequest.getPollId(),
                voteRequest.getPollChoiceId());
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }
}

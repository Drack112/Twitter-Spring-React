package com.gmail.drack.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.drack.commons.dto.response.tweet.TweetResponse;
import com.gmail.drack.commons.exception.ApiRequestException;
import com.gmail.drack.commons.util.AuthUtil;
import com.gmail.drack.constants.TweetErrorMessage;
import com.gmail.drack.models.Poll;
import com.gmail.drack.models.PollChoice;
import com.gmail.drack.models.PollChoiceVoted;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.repository.PollChoiceRepository;
import com.gmail.drack.repository.PollChoiceVotedRepository;
import com.gmail.drack.repository.PollRepository;
import com.gmail.drack.repository.TweetRepository;
import com.gmail.drack.repository.projection.TweetProjection;
import com.gmail.drack.service.PollService;
import com.gmail.drack.service.TweetService;
import com.gmail.drack.service.util.TweetServiceHelper;
import com.gmail.drack.service.util.TweetValidationHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {
    private final PollRepository pollRepository;
    private final PollChoiceRepository pollChoiceRepository;
    private final PollChoiceVotedRepository pollChoiceVotedRepository;
    private final TweetService tweetService;
    private final TweetServiceHelper tweetServiceHelper;
    private final TweetValidationHelper tweetValidationHelper;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public TweetResponse createPoll(Long pollDateTime, List<String> choices, Tweet tweet) {
        if (choices.size() < 2 || choices.size() > 4) {
            throw new ApiRequestException(TweetErrorMessage.INCORRECT_POLL_CHOICES, HttpStatus.BAD_REQUEST);
        }

        List<PollChoice> pollChoices = new ArrayList<>();
        choices.forEach(choice -> {
            if (choice.length() == 0 || choice.length() > 25) {
                throw new ApiRequestException(TweetErrorMessage.INCORRECT_CHOICE_TEXT_LENGTH, HttpStatus.BAD_REQUEST);
            }
            PollChoice pollChoice = new PollChoice(choice);
            pollChoiceRepository.save(pollChoice);
            pollChoices.add(pollChoice);
        });
        Poll poll = new Poll(LocalDateTime.now().plusMinutes(pollDateTime), tweet, pollChoices);
        pollRepository.save(poll);
        tweet.setPoll(poll);
        return tweetServiceHelper.createTweet(tweet);
    }

    @Override
    @Transactional
    public TweetProjection voteInPoll(Long tweetId, Long pollId, Long pollChoiceId) {
        Tweet tweet = tweetRepository.getTweetByPollIdAndTweetId(tweetId, pollId)
                .orElseThrow(() -> new ApiRequestException(TweetErrorMessage.POLL_NOT_FOUND, HttpStatus.NOT_FOUND));

        tweetValidationHelper.checkIsValidUserProfile(tweet.getAuthor().getId());
        Poll poll = pollRepository.getPollByPollChoiceId(pollId, pollChoiceId)
                .orElseThrow(
                        () -> new ApiRequestException(TweetErrorMessage.POLL_CHOICE_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (LocalDateTime.now().isAfter(poll.getCreatedAt())) {
            throw new ApiRequestException(TweetErrorMessage.POLL_IS_NOT_AVAILABLE, HttpStatus.BAD_REQUEST);
        }
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        if (pollChoiceVotedRepository.ifUserVoted(authUserId, pollChoiceId)) {
            throw new ApiRequestException(TweetErrorMessage.USER_VOTED_IN_POLL, HttpStatus.BAD_REQUEST);
        }
        pollChoiceVotedRepository.save(new PollChoiceVoted(authUserId, pollChoiceId));
        return tweetService.getTweetById(tweetId);
    }
}

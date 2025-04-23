package com.gmail.drack.service;

import com.gmail.drack.commons.event.UpdateTweetCountEvent;

public interface UserUpdateTweetCountService {

    void handleUpdateTweetCount(UpdateTweetCountEvent tweetCountEvent, String authId);

    void handleUpdateLikeTweetCount(UpdateTweetCountEvent tweetCountEvent, String authId);

    void handleUpdateMediaTweetCount(UpdateTweetCountEvent tweetCountEvent, String authId);
}

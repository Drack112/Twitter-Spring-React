package com.gmail.drack.service;

import com.gmail.drack.commons.event.BlockUserEvent;
import com.gmail.drack.commons.event.FollowRequestUserEvent;
import com.gmail.drack.commons.event.FollowUserEvent;
import com.gmail.drack.commons.event.MuteUserEvent;
import com.gmail.drack.commons.event.PinTweetEvent;
import com.gmail.drack.commons.event.UpdateUserEvent;
import com.gmail.drack.models.User;

public interface UserHandlerService {

    User handleNewOrUpdateUser(UpdateUserEvent userEvent);

    void handleBlockUser(BlockUserEvent userEvent, String authId);

    void handleMuteUser(MuteUserEvent userEvent, String authId);

    void handleFollowUser(FollowUserEvent userEvent, String authId);

    void handleFollowUserRequest(FollowRequestUserEvent userEvent, String authId);

    void handlePinTweet(PinTweetEvent pinTweetEvent, String authId);
}

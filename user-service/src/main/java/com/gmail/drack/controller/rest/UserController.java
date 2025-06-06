package com.gmail.drack.controller.rest;

import com.gmail.drack.commons.constants.PathConstants;
import com.gmail.drack.commons.dto.HeaderResponse;
import com.gmail.drack.dto.request.SearchTermsRequest;
import com.gmail.drack.commons.dto.response.user.CommonUserResponse;
import com.gmail.drack.commons.dto.response.user.UserResponse;
import com.gmail.drack.dto.request.UserRequest;
import com.gmail.drack.dto.response.*;
import com.gmail.drack.mapper.AuthenticationMapper;
import com.gmail.drack.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_USER)
public class UserController {

    private final UserMapper userMapper;
    private final AuthenticationMapper authenticationMapper;

    @GetMapping(PathConstants.TOKEN)
    public ResponseEntity<AuthenticationResponse> getUserByToken() {
        return ResponseEntity.ok(authenticationMapper.getUserByToken());
    }

    @GetMapping(PathConstants.USER_ID)
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userMapper.getUserById(userId));
    }

    @GetMapping(PathConstants.ALL)
    public ResponseEntity<List<UserResponse>> getUsers(@PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<UserResponse> response = userMapper.getUsers(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.RELEVANT)
    public ResponseEntity<List<UserResponse>> getRelevantUsers() {
        return ResponseEntity.ok(userMapper.getRelevantUsers());
    }

    @GetMapping(PathConstants.SEARCH_USERNAME)
    public ResponseEntity<List<UserResponse>> searchUsersByUsername(@PathVariable("username") String username,
            @PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<UserResponse> response = userMapper.searchUsersByUsername(username, pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.SEARCH_TEXT)
    public ResponseEntity<SearchResultResponse> searchByText(@PathVariable("text") String text) {
        return ResponseEntity.ok(userMapper.searchByText(text));
    }

    @PostMapping(PathConstants.SEARCH_RESULTS)
    public ResponseEntity<List<CommonUserResponse>> getSearchResults(@RequestBody SearchTermsRequest request) {
        return ResponseEntity.ok(userMapper.getSearchResults(request));
    }

    @GetMapping(PathConstants.START)
    public ResponseEntity<Boolean> startUseTwitter() {
        return ResponseEntity.ok(userMapper.startUseTwitter());
    }

    @PutMapping
    public ResponseEntity<AuthUserResponse> updateUserProfile(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userMapper.updateUserProfile(userRequest));
    }

    @GetMapping(PathConstants.SUBSCRIBE_USER_ID)
    public ResponseEntity<Boolean> processSubscribeToNotifications(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userMapper.processSubscribeToNotifications(userId));
    }

    @GetMapping(PathConstants.PIN_TWEET_ID)
    public ResponseEntity<UserPintTweetResponse> processPinTweet(@PathVariable("tweetId") Long tweetId) {
        return ResponseEntity.ok(userMapper.processPinTweet(tweetId));
    }

    @GetMapping(PathConstants.DETAILS_USER_ID)
    public ResponseEntity<UserDetailResponse> getUserDetails(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userMapper.getUserDetails(userId));
    }
}

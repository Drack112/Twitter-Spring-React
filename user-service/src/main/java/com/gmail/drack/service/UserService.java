package com.gmail.drack.service;

import com.gmail.drack.dto.request.SearchTermsRequest;
import com.gmail.drack.model.User;
import com.gmail.drack.repository.projection.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserProfileProjection getUserById(Long userId);

    Page<UserProjection> getUsers(Pageable pageable);

    List<UserProjection> getRelevantUsers();

    <T> Page<T> searchUsersByUsername(String username, Pageable pageable, Class<T> type);

    Map<String, Object> searchByText(String text);

    List<CommonUserProjection> getSearchResults(SearchTermsRequest request);

    Boolean startUseTwitter();

    AuthUserProjection updateUserProfile(User userInfo);

    Boolean processSubscribeToNotifications(Long userId);

    User processPinTweet(Long tweetId);

    UserDetailProjection getUserDetails(Long userId);
}

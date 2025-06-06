package com.gmail.drack.repository.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

public interface UserProfileProjection {

    Long getId();

    String getFullName();

    String getUsername();

    String getLocation();

    String getAbout();

    String getWebsite();

    String getCountry();

    String getBirthday();

    LocalDateTime getRegistrationDate();

    Long getTweetCount();

    Long getMediaTweetCount();

    Long getLikeCount();

    boolean isMutedDirectMessages();

    boolean isPrivateProfile();

    String getAvatar();

    String getWallpaper();

    Long getPinnedTweetId();

    Long getFollowersCount();

    Long getFollowingCount();

    @Value("#{@userServiceHelper.isUserMutedByMyProfile(target.id)}")
    boolean getIsUserMuted();

    @Value("#{@userServiceHelper.isUserBlockedByMyProfile(target.id)}")
    boolean getIsUserBlocked();

    @Value("#{@userServiceHelper.isMyProfileBlockedByUser(target.id)}")
    boolean getIsMyProfileBlocked();

    @Value("#{@userServiceHelper.isMyProfileWaitingForApprove(target.id)}")
    boolean getIsWaitingForApprove();

    @Value("#{@userServiceHelper.isUserFollowByOtherUser(target.id)}")
    boolean getIsFollower();

    @Value("#{@userServiceHelper.isMyProfileSubscribed(target.id)}")
    boolean getIsSubscriber();

    @Value("#{@userServiceHelper.getSameFollowers(target.id)}")
    List<SameFollower> getSameFollowers();
}

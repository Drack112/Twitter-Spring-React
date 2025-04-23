package com.gmail.drack.repository.projection;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.gmail.drack.commons.dto.response.tweet.TweetListResponse;
import com.gmail.drack.commons.enums.LinkCoverSize;
import com.gmail.drack.commons.enums.ReplyType;
import com.gmail.drack.commons.enums.TweetType;

public interface TweetProjection {

    Long getId();

    String getText();

    TweetType getTweetType();

    LocalDateTime getCreatedAt();

    LocalDateTime getScheduledDate();

    ReplyType getReplyType();

    GifImageProjection getGifImage();

    Long getListId();

    List<TweetImageProjection> getImages();

    String getImageDescription();

    QuoteTweetProjection getQuoteTweet();

    PollProjection getPoll();

    String getLink();

    String getLinkTitle();

    String getLinkDescription();

    String getLinkCover();

    LinkCoverSize getLinkCoverSize();

    boolean isDeleted();

    TweetAuthorProjection getAuthor();

    Long getRetweetsCount();

    Long getLikesCount();

    Long getRepliesCount();

    Long getQuotesCount();

    @Value("#{target.addressedUser?.id}")
    Long getAddressedId();

    @Value("#{target.addressedUser?.username}")
    String getAddressedUsername();

    @Value("#{target.addressedTweet?.id}")
    Long getAddressedTweetId();

    @Value("#{@tweetProjectionHelper.getTweetList(target.listId)}")
    TweetListResponse getTweetList();

    List<TaggedUserProjection> getTaggedImageUsers();

    @Value("#{@tweetProjectionHelper.isUserLikedTweet(target.id)}")
    boolean getIsTweetLiked();

    @Value("#{@tweetProjectionHelper.isUserRetweetedTweet(target.id)}")
    boolean getIsTweetRetweeted();

    @Value("#{@tweetProjectionHelper.isUserBookmarkedTweet(target.id)}")
    boolean getIsTweetBookmarked();

    @Value("#{@userServiceImpl.isUserFollowByOtherUser(target.author.id)}")
    boolean getIsUserFollowByOtherUser();

    interface QuoteTweetProjection {

        @Value("#{target.isDeleted ? null : target.id}")
        Long getId();

        @Value("#{target.isDeleted ? null : target.text}")
        String getText();

        @Value("#{target.isDeleted ? null : target.tweetType}")
        TweetType getTweetType();

        @Value("#{target.isDeleted ? null : target.createdAt}")
        LocalDateTime getCreatedAt();

        @Value("#{target.isDeleted ? null : target.link}")
        String getLink();

        @Value("#{target.isDeleted ? null : target.linkTitle}")
        String getLinkTitle();

        @Value("#{target.isDeleted ? null : target.linkDescription}")
        String getLinkDescription();

        @Value("#{target.isDeleted ? null : target.linkCover}")
        String getLinkCover();

        @Value("#{target.isDeleted ? null : target.linkCoverSize}")
        LinkCoverSize getLinkCoverSize();

        TweetAuthorProjection getAuthor();

        boolean isDeleted();
    }

    interface PollProjection {

        Long getId();

        LocalDateTime getCreatedAt();

        List<PollChoiceProjection> getPollChoices();
    }

    interface PollChoiceProjection {

        Long getId();

        String getChoice();

        @Value("#{@pollChoiceVotedRepository.getVotedUserIds(target.id)}")
        List<VotedUserProjection> getVotedUser();
    }

    interface GifImageProjection {

        Long getId();

        String getUrl();

        Long getWidth();

        Long getHeight();
    }
}

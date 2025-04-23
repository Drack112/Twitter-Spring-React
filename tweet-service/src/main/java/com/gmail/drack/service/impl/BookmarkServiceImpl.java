package com.gmail.drack.service.impl;

import org.apache.kafka.common.protocol.types.Field.Bool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.drack.commons.util.AuthUtil;
import com.gmail.drack.models.Bookmark;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.models.User;
import com.gmail.drack.repository.BookmarkRepository;
import com.gmail.drack.repository.projection.BookmarkProjection;
import com.gmail.drack.service.BookmarkService;
import com.gmail.drack.service.UserService;
import com.gmail.drack.service.util.TweetValidationHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final TweetValidationHelper tweetValidationHelper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkProjection> getUserBookmarks(Pageable pageable) {
        User authUser = userService.getAuthUser();
        return bookmarkRepository.getUserBookmarks(authUser, pageable);
    }

    @Override
    @Transactional
    public Boolean processUserBookmarks(Long tweetId) {
        Tweet tweet = tweetValidationHelper.checkValidTweet(tweetId);
        User authUser = userService.getAuthUser();
        Bookmark bookmark = bookmarkRepository.getUserBookmark(authUser, tweet);

        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
            return false;
        } else {
            Bookmark newBookmark = new Bookmark(authUser, tweet);
            bookmarkRepository.save(newBookmark);
            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean getIsTweetBookmarked(Long tweetId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return bookmarkRepository.isUserBookmarkedTweet(authUserId, tweetId);
    }

}

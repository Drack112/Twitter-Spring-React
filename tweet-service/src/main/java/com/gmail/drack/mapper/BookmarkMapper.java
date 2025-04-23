package com.gmail.drack.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.gmail.drack.commons.dto.HeaderResponse;
import com.gmail.drack.commons.dto.response.tweet.TweetResponse;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.repository.projection.BookmarkProjection;
import com.gmail.drack.repository.projection.TweetProjection;
import com.gmail.drack.service.BookmarkService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookmarkMapper {
    private final BasicMapper basicMapper;
    private final BookmarkService bookmarkService;

    public HeaderResponse<TweetResponse> getUserBookmarks(Pageable pageable) {
        Page<BookmarkProjection> bookmarks = bookmarkService.getUserBookmarks(pageable);
        List<TweetProjection> tweets = new ArrayList<>();
        bookmarks.getContent().forEach(bookmark -> tweets.add(bookmark.getTweet()));
        return basicMapper.getHeaderResponse(tweets, bookmarks.getTotalPages(), TweetResponse.class);
    }

    public Boolean processUserBookmarks(Long tweetId) {
        return bookmarkService.processUserBookmarks(tweetId);
    }

    public Boolean getIsTweetBookmarked(Long tweeId) {
        return bookmarkService.getIsTweetBookmarked(tweeId);
    }
}

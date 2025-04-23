package com.gmail.drack.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gmail.drack.repository.projection.BookmarkProjection;

public interface BookmarkService {
    Page<BookmarkProjection> getUserBookmarks(Pageable pageable);

    Boolean processUserBookmarks(Long tweetId);

    Boolean getIsTweetBookmarked(Long tweetId);
}

package com.gmail.drack.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gmail.drack.models.Bookmark;
import com.gmail.drack.models.Tweet;
import com.gmail.drack.models.User;
import com.gmail.drack.repository.projection.BookmarkProjection;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Bookmark.TweetUserId> {
    @Query("""
            SELECT CASE WHEN count(bookmark) > 0 THEN true ELSE false END
            FROM Bookmark bookmark
            WHERE bookmark.user.id = :userId
            AND bookmark.tweet.id = :tweetId
            """)
    boolean isUserBookmarkedTweet(@Param("userId") Long userId,
            @Param("tweetId") Long tweetId);

    @Query("""
            SELECT bookmark FROM Bookmark bookmark
            WHERE bookmark.user = :user
            ORDER BY bookmark.bookmarkDate DESC
            """)
    Page<BookmarkProjection> getUserBookmarks(@Param("user") User user, Pageable pageable);

    @Query("""
            SELECT bookmark FROM Bookmark bookmark
            WHERE bookmark.user = :user
            AND bookmark.tweet = :tweet
            """)
    Bookmark getUserBookmark(@Param("user") User user, @Param("tweet") Tweet tweet);
}

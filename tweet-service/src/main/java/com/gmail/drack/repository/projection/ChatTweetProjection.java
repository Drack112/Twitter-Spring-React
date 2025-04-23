package com.gmail.drack.repository.projection;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

public interface ChatTweetProjection {
    @Value("#{target.isDeleted ? null : target.id}")
    Long getId();

    @Value("#{target.isDeleted ? null : target.text}")
    String getText();

    @Value("#{target.isDeleted ? null : target.createdAt}")
    LocalDateTime getCreatedAt();

    ChatTweetUserProjection getAuthor();

    boolean isDeleted();

    interface ChatTweetUserProjection {
        Long getId();

        String getFullName();

        String getUsername();

        String getAvatar();
    }
}

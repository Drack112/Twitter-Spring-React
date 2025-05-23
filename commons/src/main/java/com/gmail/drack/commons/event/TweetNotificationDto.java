package com.gmail.drack.commons.event;

import com.gmail.drack.commons.enums.TweetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetNotificationDto {

    private Long id;
    private String text;
    private TweetType tweetType;
    private UserNotificationDto author;
}

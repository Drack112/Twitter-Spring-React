package com.gmail.drack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPintTweetResponse {

    private Long userId;
    private Long pinnedTweetId;
}

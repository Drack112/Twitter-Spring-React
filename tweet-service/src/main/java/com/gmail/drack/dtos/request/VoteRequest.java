package com.gmail.drack.dtos.request;

import lombok.Data;

@Data
public class VoteRequest {
    private Long tweetId;
    private Long pollId;
    private Long pollChoiceId;
}

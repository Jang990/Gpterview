package com.mock.interview.interview.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CurrentInterviewTopic {
    private final LocalDateTime occurredAt;
    private final Long topicId;

    static CurrentInterviewTopic createEmptyTopic(LocalDateTime occurredAt) {
        return new CurrentInterviewTopic(occurredAt, null);
    }

    static CurrentInterviewTopic create(LocalDateTime occurredAt, long topicId) {
        return new CurrentInterviewTopic(occurredAt, topicId);
    }

    private CurrentInterviewTopic(LocalDateTime occurredAt, Long topicId) {
        this.occurredAt = occurredAt;
        this.topicId = topicId;
    }
}

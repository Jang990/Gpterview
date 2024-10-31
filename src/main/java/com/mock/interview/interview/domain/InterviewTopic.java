package com.mock.interview.interview.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InterviewTopic {
    private final LocalDateTime occurredAt;
    private final Long topicId;

    static InterviewTopic createEmptyTopic(LocalDateTime occurredAt) {
        return new InterviewTopic(occurredAt, null);
    }

    static InterviewTopic create(LocalDateTime occurredAt, long topicId) {
        return new InterviewTopic(occurredAt, topicId);
    }

    private InterviewTopic(LocalDateTime occurredAt, Long topicId) {
        this.occurredAt = occurredAt;
        this.topicId = topicId;
    }
}

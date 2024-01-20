package com.mock.interview.conversation.domain;

import com.mock.interview.conversation.infrastructure.interview.dto.Message;

public record AiAnsweredEvent(long interviewId, Message message) {
}

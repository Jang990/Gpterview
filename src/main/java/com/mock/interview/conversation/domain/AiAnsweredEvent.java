package com.mock.interview.conversation.domain;

import com.mock.interview.conversation.presentation.dto.MessageDto;

public record AiAnsweredEvent(long interviewId, MessageDto message) {
}

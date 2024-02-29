package com.mock.interview.interview.domain;

import com.mock.interview.interview.presentation.dto.message.QuestionInInterviewDto;

public interface ConversationMessageBroker {
    void publish(long interviewId, QuestionInInterviewDto message);
}

package com.mock.interview.conversation.domain;

import com.mock.interview.conversation.presentation.dto.QuestionInInterviewDto;

public interface ConversationMessageBroker {
    void publish(long interviewId, QuestionInInterviewDto message);
}

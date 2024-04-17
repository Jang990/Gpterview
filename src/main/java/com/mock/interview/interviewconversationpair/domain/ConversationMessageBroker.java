package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;

import java.util.List;

public interface ConversationMessageBroker {
    void publish(long interviewId, long conversationPairId, List<InterviewQuestionResponse> questionList);
}

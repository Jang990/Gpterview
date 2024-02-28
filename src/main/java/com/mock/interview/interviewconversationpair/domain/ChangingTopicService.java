package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.stereotype.Service;

@Service
public class ChangingTopicService {
    public void changeTopic(Interview interview, InterviewConversationPair conversationPair) {
        if (interview.isTimeout())
            throw new IsAlreadyTimeoutInterviewException();

        conversationPair.changeTopic();
    }
}

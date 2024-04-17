package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.stereotype.Service;

@Service
public class ConversationRestarter {
    public void restart(Interview interview, InterviewConversationPair conversationPair) {
        verifyActiveInterview(interview);
        conversationPair.restartConversation();
    }

    public void restartOnlyAi(Interview interview, InterviewConversationPair conversationPair) {
        verifyActiveInterview(interview);
        conversationPair.restartConversationWithAi();
    }

    private static void verifyActiveInterview(Interview interview) {
        if (interview.isTimeout())
            throw new IsAlreadyTimeoutInterviewException();
    }
}

package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationRestarter {
    private final AppearedQuestionIdManager appearedQuestionIdManager;
    private final InterviewTimeHolder timeHolder;

    public void restart(Interview interview, InterviewConversationPair conversationPair) {
        verifyActiveInterview(interview);
        conversationPair.restartConversation(appearedQuestionIdManager);
    }

    public void restartOnlyAi(Interview interview, InterviewConversationPair conversationPair) {
        verifyActiveInterview(interview);
        conversationPair.restartConversationWithAi(appearedQuestionIdManager);
    }

    private void verifyActiveInterview(Interview interview) {
        if (interview.isTimeout(timeHolder.now()))
            throw new IsAlreadyTimeoutInterviewException();
    }
}

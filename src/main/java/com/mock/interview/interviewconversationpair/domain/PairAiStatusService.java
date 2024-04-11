package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.stereotype.Service;

@Service
public class PairAiStatusService {
    public void changeTopic(Interview interview, InterviewConversationPair conversationPair) {
        verifyActiveInterview(interview);

        conversationPair.restartConversation();
    }

    public void changeRecommendationToAi(Interview interview, InterviewConversationPair conversationPair) {
        verifyActiveInterview(interview);
        conversationPair.restartConversationWithAi();
    }

    private static void verifyActiveInterview(Interview interview) {
        if (interview.isTimeout())
            throw new IsAlreadyTimeoutInterviewException();
    }
}

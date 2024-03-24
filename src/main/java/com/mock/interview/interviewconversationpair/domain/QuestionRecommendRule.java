package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.stereotype.Service;

@Service
public class QuestionRecommendRule {
    private final int MIN_RECOMMENDED_SIZE = 50;
    public void selectQuestionRecommendation(InterviewConversationPair conversationPair, long relationDepartmentQuestionSize) {
        if(relationDepartmentQuestionSize < MIN_RECOMMENDED_SIZE)
            conversationPair.recommendAiQuestion();
        else
            conversationPair.recommendExistingQuestion();
    }
}

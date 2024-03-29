package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionRecommendRule {
    private final int MIN_RECOMMENDED_SIZE = 50;
    public InterviewConversationPair start(
            InterviewConversationPairRepository repository,
            Interview interview, long relationDepartmentQuestionSize
    ) {
        InterviewConversationPair conversationPair = InterviewConversationPair.create(interview);
        repository.save(conversationPair);
        selectConversationType(relationDepartmentQuestionSize, conversationPair);

        return conversationPair;
    }

    private void selectConversationType(long relationDepartmentQuestionSize, InterviewConversationPair conversationPair) {
        if(relationDepartmentQuestionSize < MIN_RECOMMENDED_SIZE)
            conversationPair.recommendAiQuestion();
        else
            conversationPair.recommendExistingQuestion();
    }
}

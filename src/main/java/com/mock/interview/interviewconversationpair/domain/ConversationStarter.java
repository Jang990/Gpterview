package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversationStarter {
    private final int MIN_RECOMMENDED_SIZE = 50;
    public InterviewConversationPair start(
            InterviewConversationPairRepository repository,
            Interview interview, long relationcategoryQuestionSize
    ) {
        InterviewConversationPair conversationPair = InterviewConversationPair.create(interview);
        repository.save(conversationPair);
        selectConversationType(relationcategoryQuestionSize, conversationPair);

        return conversationPair;
    }

    private void selectConversationType(long relationcategoryQuestionSize, InterviewConversationPair conversationPair) {
        if(relationcategoryQuestionSize < MIN_RECOMMENDED_SIZE)
            conversationPair.recommendAiQuestion();
        else
            conversationPair.recommendExistingQuestion();
    }
}

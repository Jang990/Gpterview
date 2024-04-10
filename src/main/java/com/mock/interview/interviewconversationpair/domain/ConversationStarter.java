package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversationStarter {
    public InterviewConversationPair start(
            InterviewConversationPairRepository repository,
            Interview interview
    ) {
        InterviewConversationPair conversationPair = InterviewConversationPair.create(interview);
        repository.save(conversationPair);
        conversationPair.startConversation();
        return conversationPair;
    }
}

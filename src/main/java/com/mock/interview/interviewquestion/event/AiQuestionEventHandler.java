package com.mock.interview.interviewquestion.event;

import com.mock.interview.interviewconversationpair.domain.event.AiQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.ConversationQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class AiQuestionEventHandler {
    private final InterviewConversationPairRepository conversationPairRepository;
    private final ConversationQuestionService conversationQuestionService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = AiQuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(AiQuestionRecommendedEvent event) {
        InterviewConversationPair conversationPair = conversationPairRepository.findConversation(event.interviewId(), event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        conversationQuestionService.createAiOnly(conversationPair.getInterview().getId(),conversationPair);
    }
}

package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.infra.lock.response.AiResponseAwaitLock;
import com.mock.interview.interviewconversationpair.domain.AnotherQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.ExistingQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.QuestionRecommendedService;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ExistingQuestionRecommendedEventHandler {

    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final QuestionRecommender questionRecommender;
    private final QuestionRecommendedService questionRecommendedService;

    @Async
    @AiResponseAwaitLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ExistingQuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ExistingQuestionRecommendedEvent event) {
        InterviewConversationPair pair = interviewConversationPairRepository.findByIdWithInterviewId(event.pairId(), event.interviewId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        questionRecommendedService.recommendQuestion(questionRecommender, pair);
    }

    @Async
    @AiResponseAwaitLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = AnotherQuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(AnotherQuestionRecommendedEvent event) {
        InterviewConversationPair pair = interviewConversationPairRepository.findByIdWithInterviewId(event.pairId(), event.interviewId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        questionRecommendedService.recommendAnotherQuestion(questionRecommender, pair);
    }
}

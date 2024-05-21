package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interviewconversationpair.domain.event.ConversationStartedEvent;
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
public class ConversationStartedEventHandler {
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewCacheRepository interviewCacheRepository;
    private final ConversationQuestionService conversationQuestionService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ConversationStartedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ConversationStartedEvent event) {
        InterviewInfo interviewInfo = interviewCacheRepository.findProgressingInterviewInfo(event.interviewId());
        InterviewConversationPair conversationPair = conversationPairRepository.findById(event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        try {
            conversationQuestionService.chooseQuestion(
                    interviewInfo, conversationPair,
                    event.appearedQuestionIds()
            );
        } catch (Throwable throwable) {
            interviewCacheRepository.expireInterviewInfo(event.interviewId());
        }
    }
}

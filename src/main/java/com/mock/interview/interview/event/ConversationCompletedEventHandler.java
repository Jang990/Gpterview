package com.mock.interview.interview.event;

import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.event.ConversationCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ConversationCompletedEventHandler {
    private final InterviewRepository interviewRepository;
    private final InterviewTimeHolder interviewTimeHolder;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ConversationCompletedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ConversationCompletedEvent event) {
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        interview.continueInterview(interviewTimeHolder.now());
    }

}

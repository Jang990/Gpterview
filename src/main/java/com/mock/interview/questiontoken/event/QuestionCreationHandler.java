package com.mock.interview.questiontoken.event;

import com.mock.interview.interview.infrastructure.lock.proceeding.AiResponseProcessingLock;
import com.mock.interview.interviewconversationpair.domain.PairStatusChangedToChangingEvent;
import com.mock.interview.interviewquestion.domain.QuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class QuestionCreationHandler {

    private final InterviewQuestionRepository questionRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = QuestionCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(QuestionCreatedEvent event) {
        System.out.println("??");
        InterviewQuestion question = questionRepository.findById(event.questionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);
        System.out.println("질문 생성됨!");
    }
}

package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.lock.response.AiResponseAwaitLock;
import com.mock.interview.interviewconversationpair.domain.event.ConversationStartedEvent;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.domain.ConversationQuestionSelector;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
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
public class ConversationStartedEventHandler {
    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository questionRepository;
    private final ConversationQuestionSelector conversationQuestionSelector;
    private final AiQuestionCreator aiQuestionCreator;
    private final QuestionRecommender questionRecommender;


    @Async
    @AiResponseAwaitLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ConversationStartedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ConversationStartedEvent event) {
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        Long relatedCategoryQuestionSize = questionRepository.countCategoryQuestion(interview.getCategory().getName());

        conversationQuestionSelector.select(
                aiQuestionCreator, questionRecommender, relatedCategoryQuestionSize,
                event.interviewId(), event.pairId()
        );
    }
}
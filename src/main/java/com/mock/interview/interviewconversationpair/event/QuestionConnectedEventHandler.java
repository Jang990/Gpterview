package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interviewconversationpair.domain.ConversationMessageBroker;
import com.mock.interview.interviewconversationpair.domain.event.ConversationResetEvent;
import com.mock.interview.interviewconversationpair.domain.event.QuestionConnectedEvent;
import com.mock.interview.interviewquestion.application.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.event.QuestionRecommendedEvent;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionConnectedEventHandler {
    private final ConversationMessageBroker messageBroker;
    private final InterviewQuestionRepository questionRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(
            classes = QuestionConnectedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(QuestionConnectedEvent event) {
        // TODO: 발행 실패 시 복구가 필요?
        InterviewQuestion question = questionRepository.findById(event.questionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);

        List<InterviewQuestionResponse> list = List.of(QuestionConvertor.convertInterviewQuestion(question));
        messageBroker.publish(event.interviewId(), event.pairId(),list);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(
            classes = QuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(QuestionRecommendedEvent event) {
        List<InterviewQuestionResponse> list = questionRepository.findAllById(event.questionIds()).stream()
                .map(QuestionConvertor::convertInterviewQuestion).toList();

        messageBroker.publish(event.interviewId(), event.pairId(), list);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(
            classes = ConversationResetEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ConversationResetEvent event) {
        InterviewQuestionResponse errorMessage = QuestionConvertor.convertErrorMessage(event.resetMessage());
        messageBroker.publish(event.interviewId(), event.conversationId(), List.of(errorMessage));
    }
}

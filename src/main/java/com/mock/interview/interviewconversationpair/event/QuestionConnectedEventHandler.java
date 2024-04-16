package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interviewconversationpair.domain.ConversationMessageBroker;
import com.mock.interview.interviewconversationpair.domain.event.ConversationResetEvent;
import com.mock.interview.interviewconversationpair.domain.event.QuestionConnectedEvent;
import com.mock.interview.interviewquestion.domain.event.QuestionRecommendedEvent;
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

        List<MessageDto> list = List.of(ConversationMessageBroker.createMessageDtoToPublish(question));
        messageBroker.publish(event.interviewId(), event.pairId(),list);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(
            classes = QuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(QuestionRecommendedEvent event) {
        List<MessageDto> list = questionRepository.findAllById(event.questionIds()).stream()
                .map(ConversationMessageBroker::createMessageDtoToPublish).toList();

        messageBroker.publish(event.interviewId(), event.pairId(), list);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(
            classes = ConversationResetEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ConversationResetEvent event) {
        MessageDto messageDto = new MessageDto(null, InterviewRole.SYSTEM, event.resetMessage());
        messageBroker.publish(event.interviewId(), event.conversationId(), List.of(messageDto));
    }
}

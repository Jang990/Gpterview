package com.mock.interview.interviewquestion.event;

import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.conversation.domain.UserAnsweredEvent;
import com.mock.interview.conversation.infrastructure.ConversationCacheForAiRequest;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.lock.AiResponseProcessingLock;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.domain.InterviewStartedEvent;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewquestion.domain.CreationQuestionInRunningInterviewService;
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
public class QuestionEventHandler {
    private final AIService aiService;
    private final InterviewRepository interviewRepository;
    private final InterviewCacheForAiRequest interviewCache;
    private final ConversationCacheForAiRequest conversationCache;
    private final ConversationMessageBroker conversationMessageBroker;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final CreationQuestionInRunningInterviewService creationQuestionInRunningInterviewService;

    @Async
    @AiResponseProcessingLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = InterviewStartedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void initInterviewConversation(InterviewStartedEvent event) {
        createQuestion(event.interviewId());
    }

    @Async
    @AiResponseProcessingLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = UserAnsweredEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void getAiResponse(UserAnsweredEvent event) {
        createQuestion(event.interviewId());
    }

    private void createQuestion(long interviewId) {
        Message message = QuestionRequestHelper.requestQuestion(aiService, interviewCache, conversationCache, interviewId);
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewQuestion question = creationQuestionInRunningInterviewService.save(interviewQuestionRepository, interview, message);
        conversationMessageBroker.publish(interview.getId(), new MessageDto(question.getId(), message.getRole(), message.getContent()));
    }
}

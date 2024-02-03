package com.mock.interview.conversation.event;

import com.mock.interview.conversation.domain.ChangeTopicEvent;
import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.ConversationCacheForAiRequest;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.infrastructure.lock.AiResponseProcessingLock;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ChangeTopicEventHandler {
    private final AIService aiService;
    private final InterviewRepository interviewRepository;
    private final InterviewConversationRepository conversationRepository;
    private final InterviewCacheForAiRequest interviewCache;
    private final ConversationCacheForAiRequest conversationCache;
    private final ConversationMessageBroker conversationMessageBroker;

    @Async
    @AiResponseProcessingLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ChangeTopicEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void getAiResponse(ChangeTopicEvent event) {
        System.out.println(event);
        long interviewId = event.interviewId();
        InterviewInfo interviewInfo = interviewCache.findAiInterviewSetting(interviewId);
        MessageHistory messageHistory = conversationCache.findMessageHistory(interviewId);
        Message message = aiService.changeTopic(interviewInfo, messageHistory);
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);

        AiMessageSavingHelper.saveAiMessage(conversationRepository, conversationMessageBroker, interview, message);
    }
}

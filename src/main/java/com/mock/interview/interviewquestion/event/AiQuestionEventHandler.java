package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.InterviewProgressTraceService;
import com.mock.interview.interview.infra.progress.InterviewTopicConnector;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewconversationpair.domain.event.AiQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.ConversationQuestionExceptionHandlingService;
import com.mock.interview.interviewquestion.domain.ConversationQuestionService;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.gpt.dto.MessageHistory;
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
    private final InterviewCacheRepository interviewCacheRepository;
    private final ConversationQuestionExceptionHandlingService exceptionHandlingService;
    private final InterviewProgressTraceService interviewProgressTraceService;
    private final InterviewRepository interviewRepository;
    private final ConversationCacheForAiRequest conversationCacheForAiRequest;
    private final InterviewTopicConnector topicConnector;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = AiQuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(AiQuestionRecommendedEvent event) {
        InterviewConversationPair conversationPair = conversationPairRepository.findConversation(event.interviewId(), event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        InterviewInfo interviewInfo = interviewCacheRepository.findProgressingInterviewInfo(event.interviewId());
        InterviewProgress progress = interviewProgressTraceService.trace(interviewInfo);
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        MessageHistory conversationHistory = conversationCacheForAiRequest.findCurrentConversation(interviewInfo.interviewId());

        try {
            InterviewQuestion question = conversationQuestionService.createAiOnly(interview.getUsers(), conversationPair, interviewInfo, progress, conversationHistory);
            question.linkCategory(interview.getCategory());
            question.linkPosition(interview.getPosition());
            topicConnector.connect(question, progress);
        } catch (Throwable throwable) {
            exceptionHandlingService.handle(throwable, interviewInfo.interviewId(), conversationPair.getId());
        }
    }
}

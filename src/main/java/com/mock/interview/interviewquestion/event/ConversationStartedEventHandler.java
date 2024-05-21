package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interviewconversationpair.domain.event.ConversationStartedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.domain.ConversationQuestionExceptionHandlingService;
import com.mock.interview.interviewquestion.domain.ConversationQuestionService;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationStartedEventHandler {
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewCacheRepository interviewCacheRepository;
    private final ConversationQuestionService conversationQuestionService;
    private final ConversationQuestionExceptionHandlingService exceptionHandlingService;

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
            conversationQuestionService.recommendOnly(interviewInfo, conversationPair, event.appearedQuestionIds());
        } catch (NotEnoughQuestion e) { // 추천할 질문이 부족한 경우 AI 질문 생성
            log.info("질문 추천 중 추천할 질문 부족 발생", e);
            conversationQuestionService.createAiOnly(interviewInfo, conversationPair);
        } catch (Throwable throwable) {
            exceptionHandlingService.handle(throwable, event.interviewId(), event.pairId());
            interviewCacheRepository.expireInterviewInfo(event.interviewId());
        }
    }
}

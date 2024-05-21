package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.InterviewProgressTraceService;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewconversationpair.domain.event.ConversationStartedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.*;
import com.mock.interview.interviewquestion.infra.RelatedQuestionRepository;
import com.mock.interview.interviewquestion.infra.recommend.CurrentConversationConvertor;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.questiontoken.domain.KoreaStringAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationStartedEventHandler {
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewCacheRepository interviewCacheRepository;
    private final InterviewProgressTraceService interviewProgressTraceService;
    private final RelatedQuestionRepository relatedQuestionRepository;

    private final ConversationQuestionService conversationQuestionService;
    private final ConversationQuestionExceptionHandlingService exceptionHandlingService;
    private final RelatedQuestionService relatedQuestionService;
    private final KoreaStringAnalyzer koreaStringAnalyzer;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ConversationStartedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ConversationStartedEvent event) {
        InterviewInfo interviewInfo = interviewCacheRepository.findProgressingInterviewInfo(event.interviewId());
        InterviewProgress interviewInterviewProgress = interviewProgressTraceService.trace(interviewInfo);
        InterviewConversationPair conversationPair = conversationPairRepository.findById(event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        try {
            List<QuestionMetaData> relatedQuestions = findRelatedQuestion(event, interviewInfo, interviewInterviewProgress);
            CurrentConversation currentConversation = findCurrentConversation(interviewInfo, interviewInterviewProgress);
            conversationQuestionService.recommendOnly(conversationPair, currentConversation, relatedQuestions);
        } catch (NotEnoughQuestion e) { // 추천할 질문이 부족한 경우 AI 질문 생성
            log.info("질문 추천 중 추천할 질문 부족 발생", e);
            conversationQuestionService.createAiOnly(interviewInfo, conversationPair);
        } catch (Throwable throwable) {
            exceptionHandlingService.handle(throwable, event.interviewId(), event.pairId());
            interviewCacheRepository.expireInterviewInfo(event.interviewId());
        }
    }

    @NotNull
    private CurrentConversation findCurrentConversation(InterviewInfo interviewInfo, InterviewProgress interviewInterviewProgress) {
        return CurrentConversationConvertor
                .create(conversationPairRepository, koreaStringAnalyzer,
                        interviewInfo, interviewInterviewProgress.getTopicContent()
                );
    }

    private List<QuestionMetaData> findRelatedQuestion(ConversationStartedEvent event, InterviewInfo interviewInfo, InterviewProgress interviewInterviewProgress) {
        return relatedQuestionService
                .findRelatedQuestions(
                        relatedQuestionRepository,
                        interviewInfo.profile().category(), interviewInterviewProgress,
                        event.appearedQuestionIds()
                );
    }
}

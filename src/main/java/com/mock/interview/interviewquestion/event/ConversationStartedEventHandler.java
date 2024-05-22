package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.InterviewProgressTraceService;
import com.mock.interview.interview.infra.progress.InterviewTopicConnector;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewconversationpair.domain.event.ConversationStartedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.*;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.RelatedQuestionRepository;
import com.mock.interview.interviewquestion.infra.gpt.dto.MessageHistory;
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
    private final InterviewRepository interviewRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewCacheRepository interviewCacheRepository;
    private final InterviewProgressTraceService interviewProgressTraceService;
    private final RelatedQuestionRepository relatedQuestionRepository;
    private final ConversationCacheForAiRequest conversationCacheForAiRequest;
    private final InterviewTopicConnector topicConnector;

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
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        InterviewProgress progress = interviewProgressTraceService.trace(interviewInfo);
        InterviewConversationPair conversationPair = conversationPairRepository.findById(event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        // TODO: 질문 도메인 서비스에서 레포지토리 개입을 줄였지만 코드 중복이 많아짐. 개선 필요.
        try {
            List<QuestionMetaData> relatedQuestions = findRelatedQuestion(event, interviewInfo, progress);
            CurrentConversation currentConversation = findCurrentConversation(interviewInfo, progress);
            conversationQuestionService.recommendOnly(conversationPair, currentConversation, relatedQuestions);
        } catch (NotEnoughQuestion e) { // 추천할 질문이 부족한 경우 AI 질문 생성
            log.info("질문 추천 중 추천할 질문 부족 발생", e);
            MessageHistory conversationHistory = conversationCacheForAiRequest.findCurrentConversation(interviewInfo.interviewId());
            InterviewQuestion question = conversationQuestionService.createAiOnly(interview.getUsers(), conversationPair, interviewInfo, progress, conversationHistory);
            question.linkCategory(interview.getCategory());
            question.linkPosition(interview.getPosition());
            topicConnector.connect(question, progress);
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

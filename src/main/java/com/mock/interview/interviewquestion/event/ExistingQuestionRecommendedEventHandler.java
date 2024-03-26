package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;
import com.mock.interview.interview.infrastructure.lock.proceeding.AiResponseProcessingLock;
import com.mock.interview.interviewconversationpair.domain.ExistingQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.QuestionRecommendedService;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.progress.CurrentTopicTracker;
import com.mock.interview.interviewquestion.infra.recommend.QuestionRecommender;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.questiontoken.domain.KoreaStringAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExistingQuestionRecommendedEventHandler {

    private final InterviewCacheForAiRequest interviewCache;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final QuestionRecommender recommender;
    private final QuestionRecommendedService recommendedService;
    private final KoreaStringAnalyzer stringAnalyzer;
    private final CurrentTopicTracker topicTracker;

    private final int RECOMMENDED_QUESTION_COUNT = 100;
    private final PageRequest LIMIT_ONE = PageRequest.of(0, 1);

    @Async
    @AiResponseProcessingLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ExistingQuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ExistingQuestionRecommendedEvent event) {
        InterviewInfo interview = interviewCache.findAiInterviewSetting(event.interviewId());
        InterviewConversationPair lastConversation = getLastConversation(event.interviewId());
        List<InterviewQuestion> questionForRecommend = questionRepository
                .findRandomQuestion(interview.profile().department(), PageRequest.of(0, RECOMMENDED_QUESTION_COUNT));

        recommendedService.recommend3TechQuestion(
                recommender, event.interviewId(),
                event.pairId(),createCurrentQuestion(interview, lastConversation),
                questionForRecommend
        );
    }

    private CurrentQuestion createCurrentQuestion(InterviewInfo interview, InterviewConversationPair lastConversation) {
        if (lastConversation == null || lastConversation.getQuestion() == null) {
            return new CurrentQuestion(null,null, topicTracker.trace(interview), interview.profile().field());
        }

        return new CurrentQuestion(
                lastConversation.getAnswer().getId(),
                stringAnalyzer.extractNecessaryTokens(lastConversation.getAnswer().getAnswer()),
                topicTracker.trace(interview), interview.profile().field()
        );
    }

    private InterviewConversationPair getLastConversation(long interviewId) {
        List<InterviewConversationPair> lastCompletedConversation = conversationPairRepository.findLastCompletedConversation(interviewId, LIMIT_ONE);
        if(lastCompletedConversation.isEmpty())
            return null;
        return lastCompletedConversation.get(0);
    }
}

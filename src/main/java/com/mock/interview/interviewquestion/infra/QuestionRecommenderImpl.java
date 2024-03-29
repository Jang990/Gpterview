package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interview.infra.InterviewCacheForAiRequest;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.RecommendationTarget;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import com.mock.interview.interviewquestion.domain.Top3Question;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionTechLink;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.progress.CurrentTopicTracker;
import com.mock.interview.interviewquestion.infra.recommend.QuestionRankingService;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.questiontoken.domain.KoreaStringAnalyzer;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 변경이 있을 때 같이 볼 것
 * @see com.mock.interview.interviewquestion.infra.cache.RecommendationCacheAspect
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRecommenderImpl implements QuestionRecommender {

    private final Pageable LIMIT_ONE = PageRequest.of(0, 1);
    private final InterviewCacheForAiRequest interviewCache;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final QuestionRankingService recommender;
    private final KoreaStringAnalyzer stringAnalyzer;
    private final CurrentTopicTracker topicTracker;

    private final int RECOMMENDED_QUESTION_COUNT = 100;
    private final int TOP_3 = 3;

    @Override
    public Top3Question recommendTop3(RecommendationTarget target) {
        InterviewInfo interview = interviewCache.findAiInterviewSetting(target.interviewId());
        InterviewConversationPair targetConversation = getLastConversation(target.interviewId());
        List<QuestionMetaData> questionForRecommend = questionRepository
                .findRandomQuestion(interview.profile().department(), PageRequest.of(0, RECOMMENDED_QUESTION_COUNT))
                .stream().map(this::convertQuestion).toList();

        try {
            List<Long> result = recommender
                    .recommendTechQuestion(TOP_3, createCurrentQuestion(interview, targetConversation), questionForRecommend);
            return new Top3Question(result);
        } catch (NotEnoughQuestion e) {
            log.warn("추천 기능 예외 발생", e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Top3Question retryRecommendation(RecommendationTarget target) {
        // 현재 저장된 캐시를 만료하고 새롭게 저장 - AOP 처리
        return recommendTop3(target);
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

    private QuestionMetaData convertQuestion(InterviewQuestion question) {
        return new QuestionMetaData(
                question.getId(),
                (question.getParentQuestion() == null) ? null : question.getParentQuestion().getId(),
                question.getAppliedJob().getName(), convertTechLink(question),
                question.getQuestionToken().getResult(), question.getLikes()
        );
    }

    private List<String> convertTechLink(InterviewQuestion q) {
        return q.getTechLink().stream().map(QuestionTechLink::getTechnicalSubjects).map(TechnicalSubjects::getName).toList();
    }

    private InterviewConversationPair getLastConversation(long interviewId) {
        List<InterviewConversationPair> lastCompletedConversation = conversationPairRepository.findLastCompletedConversation(interviewId, LIMIT_ONE);
        if(lastCompletedConversation.isEmpty())
            return null;
        return lastCompletedConversation.get(0);
    }
}

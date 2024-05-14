package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationQuestionService {
    private final AiQuestionCreator aiCreator;
    private final QuestionRecommender recommender;

    private final int SINGLE = 1, FIRST_IDX = 0;

    public void chooseQuestion(
            long interviewId, InterviewConversationPair pair,
            List<Long> appearedQuestionIds
    ) {
        try {
            recommendOnly(interviewId, pair, appearedQuestionIds);
        } catch (NotEnoughQuestion e) { // 추천할 질문이 부족한 경우 AI 질문 생성
            log.info("질문 추천 중 추천할 질문 부족 발생", e);
            createAiOnly(interviewId, pair);
        } catch (Throwable throwable) {
            ConversationQuestionExceptionHandlingHelper.handle(throwable, interviewId, pair.getId());
            throw throwable;
        }
    }

    public void recommendOnly(long interviewId, InterviewConversationPair pair, List<Long> appearedQuestionIds) throws NotEnoughQuestion {
        RecommendationTarget target = new RecommendationTarget(interviewId, pair.getId());
        InterviewQuestion question = recommender.recommend(SINGLE, target, appearedQuestionIds).get(FIRST_IDX);
        Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
    }

    public void createAiOnly(long interviewId, InterviewConversationPair pair) {
        try {
            InterviewQuestion question = aiCreator.create(interviewId, AiQuestionCreator.selectCreationOption(pair));
            Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
        } catch (Throwable throwable) {
            ConversationQuestionExceptionHandlingHelper.handle(throwable, interviewId, pair.getId());
            throw throwable;
        }
    }
}

package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionRecommendedService {

    public void recommendQuestion(
            QuestionRecommender questionRecommender,
            InterviewConversationPair pair
    ) {
        if(pair.isRecommendationDeniedState())
            throw new IllegalStateException();

        long interviewId = pair.getInterview().getId();
        long pairId = pair.getId();
        try {
            List<Long> questionIds = questionRecommender.recommendTop3(new RecommendationTarget(interviewId, pairId)).questions();
            Events.raise(new QuestionRecommendedEvent(interviewId, pairId, questionIds));
        } catch (Exception e) {
            log.warn("추천 기능 예외 발생", e);
            Events.raise(new RaisedQuestionErrorEvent(interviewId, e.getMessage()));
            throw new IllegalArgumentException();
        }
    }
}

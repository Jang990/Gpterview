package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import org.springframework.stereotype.Service;

@Service
public class ConversationQuestionSelector {
    private final int MIN_RECOMMENDED_SIZE = 50;
    public void select(
            AiQuestionCreator aiCreator, QuestionRecommender recommender,
            long relatedCategoryQuestionSize, long interviewId, long pairId
    ) {
        // TODO: InterviewQuestion을 반환하도록 수정하기.
        if (hasEnoughQuestion(relatedCategoryQuestionSize)) {
            recommender.recommendTop3(new RecommendationTarget(interviewId, pairId));
            return;
        }

        aiCreator.create(interviewId, AiQuestionCreator.CreationOption.NORMAL);
    }

    private boolean hasEnoughQuestion(long relatedCategoryQuestionSize) {
        return relatedCategoryQuestionSize < MIN_RECOMMENDED_SIZE;
    }

}

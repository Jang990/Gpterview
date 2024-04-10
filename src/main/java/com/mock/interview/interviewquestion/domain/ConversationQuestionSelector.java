package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.event.QuestionRecommendedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationQuestionSelector {
    private final int MIN_RECOMMENDED_SIZE = 50;
    private final int SINGLE = 1;
    private final int FIRST_IDX = 0;
    public void select(
            AiQuestionCreator aiCreator, QuestionRecommender recommender,
            long relatedCategoryQuestionSize, long interviewId, long pairId
    ) {
        InterviewQuestion question;
        if (hasEnoughQuestion(relatedCategoryQuestionSize)) {
            RecommendationTarget target = new RecommendationTarget(interviewId, pairId);
            question = recommender.recommend(SINGLE, target).get(FIRST_IDX);
        } else {
            question = aiCreator.createQuestion(interviewId, AiQuestionCreator.CreationOption.NORMAL);
        }

        Events.raise(new ConversationQuestionCreatedEvent(pairId, question.getId()));
    }

    private boolean hasEnoughQuestion(long relatedCategoryQuestionSize) {
        return relatedCategoryQuestionSize < MIN_RECOMMENDED_SIZE;
    }

}

package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ConversationQuestionService {
    private final int MIN_RECOMMENDED_SIZE = 50;
    private final int SINGLE = 1;
    private final int FIRST_IDX = 0;
    public void chooseMethod(
            AiQuestionCreator aiCreator, QuestionRecommender recommender,
            long relatedCategoryQuestionSize, long interviewId, InterviewConversationPair pair
    ) {
        if (hasEnoughQuestion(relatedCategoryQuestionSize)) {
            recommendOnly(recommender, interviewId, pair);
            return;
        }

        createAiOnly(aiCreator, interviewId, pair);
    }

    public void recommendOnly(QuestionRecommender recommender, long interviewId, InterviewConversationPair pair) {
        try {
            RecommendationTarget target = new RecommendationTarget(interviewId, pair.getId());
            InterviewQuestion question = recommender.recommend(SINGLE, target).get(FIRST_IDX);
            Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
        } catch (Throwable throwable) {
            ConversationQuestionExceptionHandlingHelper.handle(throwable, interviewId, pair.getId());
            throw throwable;
        }
    }

    public void createAiOnly(AiQuestionCreator aiCreator, long interviewId, InterviewConversationPair pair) {
        try {
            InterviewQuestion question = aiCreator.create(interviewId, AiQuestionCreator.selectCreationOption(pair));
            Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
        } catch (Throwable throwable) {
            ConversationQuestionExceptionHandlingHelper.handle(throwable, interviewId, pair.getId());
            throw throwable;
        }
    }

    private boolean hasEnoughQuestion(long relatedCategoryQuestionSize) {
        return relatedCategoryQuestionSize >= MIN_RECOMMENDED_SIZE;
    }
}

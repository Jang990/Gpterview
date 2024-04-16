package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.event.CriticalQuestionSelectionErrorEvent;
import com.mock.interview.interviewquestion.domain.event.QuestionSelectionErrorEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;


@Service
@Slf4j
public class ConversationQuestionSelector {
    private final int MIN_RECOMMENDED_SIZE = 50;
    private final int SINGLE = 1;
    private final int FIRST_IDX = 0;
    public void select(
            AiQuestionCreator aiCreator, QuestionRecommender recommender,
            long relatedCategoryQuestionSize, long interviewId, InterviewConversationPair pair
    ) {
        if (hasEnoughQuestion(relatedCategoryQuestionSize)) {
            RecommendationTarget target = new RecommendationTarget(interviewId, pair.getId());
            InterviewQuestion question = recommender.recommend(SINGLE, target).get(FIRST_IDX);
            Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
            return;
        }

        try {
            InterviewQuestion question = aiCreator.create(interviewId, AiQuestionCreator.selectCreationOption(pair));
            Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
        } catch (RuntimeException e) {
            log.info("AI 요청기 오류 발생", e);
            handleException(e, interviewId, pair.getId());
        } catch (Exception e) {
            Events.raise(new QuestionSelectionErrorEvent(interviewId, pair.getId()));
        }
    }

    private void handleException(RuntimeException exception, long interviewId, long conversationId) {
        if (isCriticalException(exception)) {
            Events.raise(new CriticalQuestionSelectionErrorEvent(interviewId, conversationId));
        } else {
            Events.raise(new QuestionSelectionErrorEvent(interviewId, conversationId));
        }

        throw exception;
    }

    private boolean isCriticalException(RuntimeException exception) {
        if(exception instanceof HttpServerErrorException)
            return true;
        if(exception instanceof HttpClientErrorException e)
            return e.getStatusCode() != HttpStatus.TOO_MANY_REQUESTS;

        return false;
    }

    private boolean hasEnoughQuestion(long relatedCategoryQuestionSize) {
        return relatedCategoryQuestionSize >= MIN_RECOMMENDED_SIZE;
    }

}

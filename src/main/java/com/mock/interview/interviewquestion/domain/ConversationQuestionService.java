package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
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
    private final ConversationQuestionExceptionHandlingService exceptionHandlingService;

    private final int SINGLE = 1, FIRST_IDX = 0;

    public void recommendOnly(InterviewInfo interviewInfo, InterviewConversationPair pair, List<Long> appearedQuestionIds) throws NotEnoughQuestion {
        RecommendationTarget target = new RecommendationTarget(interviewInfo.interviewId(), pair.getId());
        InterviewQuestion question = recommender.recommend(SINGLE, target, appearedQuestionIds).get(FIRST_IDX);
        Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
    }

    public void createAiOnly(InterviewInfo interviewInfo, InterviewConversationPair pair) {
        try {
            InterviewQuestion question = aiCreator.create(interviewInfo.interviewId(), AiQuestionCreator.selectCreationOption(pair));
            Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
        } catch (Throwable throwable) {
            exceptionHandlingService.handle(throwable, interviewInfo.interviewId(), pair.getId());
            throw throwable;
        }
    }
}

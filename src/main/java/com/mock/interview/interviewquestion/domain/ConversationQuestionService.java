package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
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

    public void recommendOnly(
            InterviewConversationPair pair,
            CurrentConversation currentConversation,
            List<QuestionMetaData> relatedQuestions
    ) throws NotEnoughQuestion {
        long recommendedQuestionId = recommender.recommend(SINGLE, currentConversation, relatedQuestions).get(FIRST_IDX);
        Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), recommendedQuestionId));
    }

    public void createAiOnly(InterviewInfo interviewInfo, InterviewConversationPair pair) {
        InterviewQuestion question = aiCreator.create(interviewInfo.interviewId(), AiQuestionCreator.selectCreationOption(pair));
        Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
    }
}

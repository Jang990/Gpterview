package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.gpt.dto.MessageHistory;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import com.mock.interview.user.domain.model.Users;
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
    private final InterviewQuestionRepository repository;

    private final int SINGLE = 1, FIRST_IDX = 0;

    public void recommendOnly(
            InterviewConversationPair pair,
            CurrentConversation currentConversation,
            List<QuestionMetaData> relatedQuestions
    ) throws NotEnoughQuestion {
        long recommendedQuestionId = recommender.recommend(SINGLE, currentConversation, relatedQuestions).get(FIRST_IDX);
        Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), recommendedQuestionId));
    }

    public InterviewQuestion createAiOnly(
            Users users, InterviewConversationPair pair,
            InterviewInfo interviewInfo, InterviewProgress progress,
            MessageHistory history
    ) {
        String aiQuestion = aiCreator.create(interviewInfo, progress, history, AiQuestionCreator.selectCreationOption(pair));

        QuestionType type = QuestionConvertor.convert(progress.phase());
        InterviewQuestion question = InterviewQuestion.create(
                repository, aiQuestion, users,
                type, true
        );
        Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
        return question;
    }
}

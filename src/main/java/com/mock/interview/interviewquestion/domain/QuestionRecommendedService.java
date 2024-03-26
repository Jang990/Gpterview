package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionTechLink;
import com.mock.interview.interviewquestion.infra.recommend.QuestionRecommender;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class QuestionRecommendedService {
    private final int RECOMMENDED_QUESTION_3 = 3;

    public void recommend3TechQuestion(
            QuestionRecommender recommender,
            long interviewId, long pairId, CurrentQuestion currentQuestion,
            List<InterviewQuestion> questionForRecommend
    ) {
        List<QuestionMetaData> questionMetaDataList = questionForRecommend.stream().map(this::convertQuestion).toList();

        try {
            List<Long> questionIds = recommender.recommendTechQuestion(RECOMMENDED_QUESTION_3, currentQuestion, questionMetaDataList);
            Events.raise(new QuestionRecommendedEvent(interviewId, pairId, questionIds));
        } catch (NotEnoughQuestion e) {
            log.warn("추천 기능 예외 발생", e);
            Events.raise(new RaisedQuestionErrorEvent(interviewId, e.getMessage()));
            throw new IllegalArgumentException();
        }
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
}

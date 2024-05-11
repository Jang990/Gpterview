package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.Top3Question;

import java.util.List;

public interface QuestionRecommender {
    List<InterviewQuestion> recommend(int recommendationSize, RecommendationTarget targetDto, List<Long> appearedQuestionIds) throws NotEnoughQuestion;

    Top3Question recommendTop3(RecommendationTarget targetDto, List<Long> appearedQuestionIds) throws NotEnoughQuestion;

    Top3Question retryRecommendation(RecommendationTarget targetDto, List<Long> appearedQuestionIds) throws NotEnoughQuestion;
}

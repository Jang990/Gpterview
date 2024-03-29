package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.Top3Question;

public interface QuestionRecommender {
    Top3Question recommendTop3(RecommendationTarget targetDto);

    Top3Question retryRecommendation(RecommendationTarget targetDto);
}

package com.mock.interview.interviewquestion.domain;

import java.util.List;

public interface QuestionRecommender {
    Top3Question recommendTop3(RecommendationTarget targetDto);

    Top3Question retryRecommendation(RecommendationTarget targetDto);
}

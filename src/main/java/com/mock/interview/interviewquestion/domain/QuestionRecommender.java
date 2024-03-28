package com.mock.interview.interviewquestion.domain;

import java.util.List;

public interface QuestionRecommender {
    List<Long> recommendTop3(RecommendationTarget targetDto);

    List<Long> retryRecommendation(RecommendationTarget targetDto);
}

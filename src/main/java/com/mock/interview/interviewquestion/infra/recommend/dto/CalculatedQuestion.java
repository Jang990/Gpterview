package com.mock.interview.interviewquestion.infra.recommend.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CalculatedQuestion implements Comparable<CalculatedQuestion> {
    private long questionId;
    private QuestionMetaData question;
    private double recommendedScore;

    public CalculatedQuestion(QuestionMetaData question, double recommendedScore) {
        this.questionId = question.getId();
        this.question = question;
        this.recommendedScore = recommendedScore;
    }

    public boolean isMoreRecommended(CalculatedQuestion cq) {
        return recommendedScore >= cq.getRecommendedScore();
    }

    @Override
    public int compareTo(CalculatedQuestion cq) {
        return Double.compare(this.recommendedScore, cq.recommendedScore);
    }
}

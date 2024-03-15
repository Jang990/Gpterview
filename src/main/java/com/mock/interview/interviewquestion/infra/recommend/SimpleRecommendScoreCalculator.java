package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SimpleRecommendScoreCalculator {
    private final double TF_IDF_WEIGHT = 0.3,
            FIELD_WEIGHT = 0.1,
            TECH_WEIGHT = 0.15,
            CHILD_WEIGHT = 0.2,
            LIKE_WEIGHT = 0.15;

    private final int FULL_LIKES_COUNT = 20;
    private final int MAX_TECH_BOUND = 5;

    public double calculateScore(CurrentQuestion currentQuestion, QuestionMetaData question) {
        double result = 0;
        result += calculateTFDIFScore(TF_IDF_WEIGHT, currentQuestion.beforeQuestionContent(), question.getContent());
        result += calculateLikesScore(LIKE_WEIGHT, question.getLikes());
        result += calculateFieldMatchedScore(FIELD_WEIGHT, currentQuestion.field(), question.getField());
        result += calculateTechContainScore(TECH_WEIGHT, question.getTech(), currentQuestion.tech());
        result += calculateChildQuestionScore(CHILD_WEIGHT, currentQuestion.beforeQuestionId(), question.getParentQuestionId());
        print(currentQuestion, question);
        return result;
    }

    private void print(CurrentQuestion currentQuestion, QuestionMetaData question) {
        System.out.print(String.format("%.4f\t", calculateTFDIFScore(TF_IDF_WEIGHT, currentQuestion.beforeQuestionContent(), question.getContent())));
        System.out.print(String.format("%.4f\t", calculateLikesScore(LIKE_WEIGHT, question.getLikes())));
        System.out.print(String.format("%.4f\t", calculateFieldMatchedScore(FIELD_WEIGHT, currentQuestion.field(), question.getField())));
        System.out.print(String.format("%.4f\t", calculateTechContainScore(TECH_WEIGHT, question.getTech(), currentQuestion.tech())));
        System.out.print(String.format("%.4f\t", calculateChildQuestionScore(CHILD_WEIGHT, currentQuestion.beforeQuestionId(), question.getParentQuestionId())));
        System.out.println();
    }

    private double calculateTFDIFScore(double weight, String base, String content) {
        boolean eitherEmpty = !StringUtils.hasText(base) || !StringUtils.hasText(content);
        if(eitherEmpty)
            return 0;

        return weight;
    }

    private double calculateLikesScore(double weight, long likes) {
        if(likes >= FULL_LIKES_COUNT)
            return weight;
        return (double) likes / FULL_LIKES_COUNT * weight;
    }

    private double calculateChildQuestionScore(double weight, long questionId, Long parentQuestionId) {
        boolean isMatch = parentQuestionId != null && questionId == parentQuestionId;
        return isMatch ? weight : 0;
    }

    private double calculateTechContainScore(double weight, List<String> questionTech, String tech) {
        if(!questionTech.contains(tech))
            return 0;

        int numberOfRelationalTech = questionTech.size();
        if (numberOfRelationalTech <= MAX_TECH_BOUND) {
            return weight;
        }

        return (double) 1 / numberOfRelationalTech * weight;
    }

    private double calculateFieldMatchedScore(double weight, String field, String questionField) {
        return field.equals(questionField) ? weight : 0;
    }
}

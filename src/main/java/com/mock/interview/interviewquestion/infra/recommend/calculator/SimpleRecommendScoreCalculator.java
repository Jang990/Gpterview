package com.mock.interview.interviewquestion.infra.recommend.calculator;

import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleRecommendScoreCalculator {
    private final double TF_IDF_WEIGHT = 0.5,
            FIELD_WEIGHT = 0.05,
            TECH_WEIGHT = 0.15,
            CHILD_WEIGHT = 0.15,
            LIKE_WEIGHT = 0.15;

    private final int FULL_LIKES_COUNT = 20;
    private final int MAX_TECH_BOUND = 5;

    public double calculateScore(CurrentQuestion currentQuestion, QuestionMetaData target) {
        double result = 0;
        result += calculateTFDIFScore(TF_IDF_WEIGHT, target.getCosineSimilarity());
        result += calculateLikesScore(LIKE_WEIGHT, target.getLikes());
        result += calculateFieldMatchedScore(FIELD_WEIGHT, target.getField(), currentQuestion.field());
        result += calculateTechContainScore(TECH_WEIGHT, target.getTech(), currentQuestion.tech());
        result += calculateChildQuestionScore(CHILD_WEIGHT, target.getParentQuestionId(), currentQuestion.beforeQuestionId());
        print(currentQuestion, target, result);
        return result;
    }

    private void print(CurrentQuestion currentQuestion, QuestionMetaData question, double result) {
        System.out.print(question.getId() + " : ");
        System.out.print(String.format("%.4f\t", calculateTFDIFScore(TF_IDF_WEIGHT, question.getCosineSimilarity())));
        System.out.print(String.format("%.4f\t", calculateLikesScore(LIKE_WEIGHT, question.getLikes())));
        System.out.print(String.format("%.4f\t", calculateFieldMatchedScore(FIELD_WEIGHT, currentQuestion.field(), question.getField())));
        System.out.print(String.format("%.4f\t", calculateTechContainScore(TECH_WEIGHT, question.getTech(), currentQuestion.tech())));
        System.out.print(String.format("%.4f\t", calculateChildQuestionScore(CHILD_WEIGHT, currentQuestion.beforeQuestionId(), question.getParentQuestionId())));
        System.out.print(String.format("%.7f\t", result));
        System.out.print(question.getNecessaryWords());
        System.out.println();
    }

    private double calculateTFDIFScore(double weight, double targetCosineSimilarity) {
        if(Double.isNaN(targetCosineSimilarity) || Double.isInfinite(targetCosineSimilarity))
            return 0.0;
        if(targetCosineSimilarity >= 1)
            return weight;
        return weight * targetCosineSimilarity;
    }

    private double calculateLikesScore(double weight, long likes) {
        if(likes >= FULL_LIKES_COUNT)
            return weight;
        return (double) likes / FULL_LIKES_COUNT * weight;
    }

    private double calculateChildQuestionScore(double weight, Long questionId, Long parentQuestionId) {
        boolean isMatch = questionId != null && questionId.equals(parentQuestionId);
        return isMatch ? weight : 0.0;
    }

    private double calculateTechContainScore(double weight, List<String> questionTech, String tech) {
        if(questionTech == null || questionTech.isEmpty()
                || tech == null || !questionTech.contains(tech))
            return 0.0;

        int numberOfRelationalTech = questionTech.size();
        if (numberOfRelationalTech <= MAX_TECH_BOUND) {
            return weight;
        }

        return 1.0 / numberOfRelationalTech * weight;
    }

    private double calculateFieldMatchedScore(double weight, String field, String questionField) {
        if(field == null || questionField == null)
            return 0.0;
        return field.equals(questionField) ? weight : 0.0;
    }
}

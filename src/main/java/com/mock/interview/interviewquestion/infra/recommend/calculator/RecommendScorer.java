package com.mock.interview.interviewquestion.infra.recommend.calculator;

import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendScorer {
    private final double TF_IDF_WEIGHT = 0.5,
            CHILD_WEIGHT = 0.3,
            LIKE_WEIGHT = 0.2;

    private final int FULL_LIKES_COUNT = 20;
    private final int MAX_TECH_BOUND = 5;

    public double calculateScore(CurrentConversation currentConversation, QuestionMetaData target) {
        double result = 0;
        result += calculateTFDIFScore(TF_IDF_WEIGHT, target.getCosineSimilarity());
        result += calculateLikesScore(LIKE_WEIGHT, target.getLikes());
        result += calculateChildQuestionScore(CHILD_WEIGHT, target.getParentQuestionId(), currentConversation.beforeQuestionId());
//        print(currentQuestion, target, result);
        return result;
    }

    /*private void print(CurrentQuestion currentQuestion, QuestionMetaData question, double result) {
        System.out.print(question.getId() + " : ");
        System.out.print(String.format("%.4f\t", calculateTFDIFScore(TF_IDF_WEIGHT, question.getCosineSimilarity())));
        System.out.print(String.format("%.4f\t", calculateLikesScore(LIKE_WEIGHT, question.getLikes())));
        System.out.print(String.format("%.4f\t", calculateFieldMatchedScore(FIELD_WEIGHT, currentQuestion.field(), question.getField())));
        System.out.print(String.format("%.4f\t", calculateTechContainScore(TECH_WEIGHT, question.getTech(), currentQuestion.tech())));
        System.out.print(String.format("%.4f\t", calculateChildQuestionScore(CHILD_WEIGHT, currentQuestion.beforeQuestionId(), question.getParentQuestionId())));
        System.out.print(String.format("%.7f\t", result));
        System.out.print(question.getNecessaryWords());
        System.out.println();
    }*/

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
}

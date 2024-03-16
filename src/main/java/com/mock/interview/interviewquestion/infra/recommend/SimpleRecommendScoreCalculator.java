package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleRecommendScoreCalculator {
    private final TFIDFCalculator tfidfCalculator;

    private final double TF_IDF_WEIGHT = 0.5,
            FIELD_WEIGHT = 0.05,
            TECH_WEIGHT = 0.15,
            CHILD_WEIGHT = 0.15,
            LIKE_WEIGHT = 0.15;

    private final int FULL_LIKES_COUNT = 20;
    private final int MAX_TECH_BOUND = 5;

    public double calculateScore(CurrentQuestion currentQuestion, QuestionMetaData question, List<List<String>> otherQuestionsContent) {
        double result = 0;
        result += calculateTFDIFScore(TF_IDF_WEIGHT, currentQuestion.beforeQuestionContent(), question.getNecessaryWords(), otherQuestionsContent);
        result += calculateLikesScore(LIKE_WEIGHT, question.getLikes());
        result += calculateFieldMatchedScore(FIELD_WEIGHT, currentQuestion.field(), question.getField());
        result += calculateTechContainScore(TECH_WEIGHT, question.getTech(), currentQuestion.tech());
        result += calculateChildQuestionScore(CHILD_WEIGHT, currentQuestion.beforeQuestionId(), question.getParentQuestionId());
        print(currentQuestion, question, result, otherQuestionsContent);
        return result;
    }

    private void print(CurrentQuestion currentQuestion, QuestionMetaData question, double result, List<List<String>> otherQuestionsContent) {
        System.out.print(String.format("%.4f\t", calculateTFDIFScore(TF_IDF_WEIGHT, currentQuestion.beforeQuestionContent(), question.getNecessaryWords(), otherQuestionsContent)));
        System.out.print(String.format("%.4f\t", calculateLikesScore(LIKE_WEIGHT, question.getLikes())));
        System.out.print(String.format("%.4f\t", calculateFieldMatchedScore(FIELD_WEIGHT, currentQuestion.field(), question.getField())));
        System.out.print(String.format("%.4f\t", calculateTechContainScore(TECH_WEIGHT, question.getTech(), currentQuestion.tech())));
        System.out.print(String.format("%.4f\t", calculateChildQuestionScore(CHILD_WEIGHT, currentQuestion.beforeQuestionId(), question.getParentQuestionId())));
        System.out.print(String.format("%.7f\t", result));
    }

    private double calculateTFDIFScore(double weight, List<String> base, List<String> content, List<List<String>> otherQuestionsContent) {
        if(base.isEmpty() || content.isEmpty())
            return 0;

        double tfidfScoreSum = 0;
        int numOfWords = base.size();
        for (String word : base) {
            tfidfScoreSum += tfidfCalculator.tfIdf(content, otherQuestionsContent, word);
        }

        double averageTfidfScore = tfidfScoreSum / numOfWords;
        if(averageTfidfScore >= 1)
            return weight;

        return weight * averageTfidfScore;
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

package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.calculator.CosineSimilarityCalculator;
import com.mock.interview.interviewquestion.infra.calculator.SimpleRecommendScoreCalculator;
import com.mock.interview.interviewquestion.infra.calculator.TFIDFCalculator;
import com.mock.interview.interviewquestion.infra.recommend.dto.CalculatedQuestion;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionRecommender {
    private final SimpleRecommendScoreCalculator simpleScoreCalculator;
    private final TFIDFCalculator tfidfCalculator;
    private final CosineSimilarityCalculator cosineSimilarityCalculator;

    public List<Long> recommendTechQuestion(
            int maxRecommendedSize, CurrentQuestion currentQuestion,
            List<QuestionMetaData> departmentMatchedQuestions
    ) throws NotEnoughQuestion {
        boolean hasEnoughQuestion = maxRecommendedSize < departmentMatchedQuestions.size();
        if(!hasEnoughQuestion)
            throw new NotEnoughQuestion();

        initQuestionCosineSimilarity(currentQuestion, departmentMatchedQuestions);
        PriorityQueue<CalculatedQuestion> recommendedQuestions = new PriorityQueue<>();
        for (QuestionMetaData question : departmentMatchedQuestions) {
            double score = simpleScoreCalculator.calculateScore(currentQuestion, question);
            CalculatedQuestion calculatedQuestion = new CalculatedQuestion(question, score);

            boolean hasSpaceInQueue = recommendedQuestions.isEmpty() || recommendedQuestions.size() < maxRecommendedSize;
            if (hasSpaceInQueue) {
                recommendedQuestions.offer(calculatedQuestion);
                continue;
            }

            CalculatedQuestion minScoreQuestion = recommendedQuestions.peek();
            if (minScoreQuestion.isMoreRecommended(calculatedQuestion))
                continue;

            recommendedQuestions.poll();
            recommendedQuestions.offer(calculatedQuestion);
        }

        return recommendedQuestions.stream().map(CalculatedQuestion::getQuestion)
                .map(QuestionMetaData::getId).toList();
    }

    /** TF-IDF 계산을 통한 코사인 유사도 설정 */
    private void initQuestionCosineSimilarity(CurrentQuestion currentQuestion, List<QuestionMetaData> departmentMatchedQuestions) {
        List<String> base = currentQuestion.beforeQuestionContent();
        List<List<String>> allQuestions = departmentMatchedQuestions.stream()
                .map(QuestionMetaData::getNecessaryWords).collect(Collectors.toList());
        allQuestions.add(base);

        double[] baseVector  = tfidfCalculator.tfIdfVector(base, base, allQuestions);
        int ADDED_BASE_QUESTION_SIZE = 1;
        for (int i = 0; i < allQuestions.size() - ADDED_BASE_QUESTION_SIZE; i++) { // 전체 문서 순회
            double[] targetVector = tfidfCalculator.tfIdfVector(base, allQuestions.get(i), allQuestions);
            double cosineSimilarity = cosineSimilarityCalculator.calculate(baseVector, targetVector);
            departmentMatchedQuestions.get(i).setCosineSimilarity(cosineSimilarity);
        }
    }

}

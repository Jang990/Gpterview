package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.dto.CalculatedQuestion;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
public class QuestionRecommender {
    private final SimpleRecommendScoreCalculator simpleScoreCalculator;

    public List<Long> recommendTechQuestion(
            int maxRecommendedSize, CurrentQuestion currentQuestion,
            List<QuestionMetaData> departmentMatchedQuestions
    ) throws NotEnoughQuestion {
        boolean hasEnoughQuestion = maxRecommendedSize < departmentMatchedQuestions.size();
        if(!hasEnoughQuestion)
            throw new NotEnoughQuestion();

        PriorityQueue<CalculatedQuestion> recommendedQuestions = new PriorityQueue<>();
        List<List<String>> questionContents = departmentMatchedQuestions.stream()
                .map(QuestionMetaData::getNecessaryWords).toList();

        for (QuestionMetaData question : departmentMatchedQuestions) {
            double score = simpleScoreCalculator.calculateScore(currentQuestion, question, questionContents);
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

}

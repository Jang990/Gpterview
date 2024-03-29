package com.mock.interview.interviewquestion.application;

import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.Top3Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRecommendationService {
    private final InterviewQuestionRepository questionRepository;
    private final QuestionRecommender recommender;

    public List<MessageDto> findRecommendation(long interviewId, long pairId) {
        Top3Question top3Question = recommender.recommendTop3(new RecommendationTarget(interviewId, pairId));
        List<InterviewQuestion> recommendedQuestions = questionRepository.findAllById(top3Question.questions());
        return recommendedQuestions.stream().map(rq -> MessageDto.createQuestion(rq.getId(), rq.getQuestion())).toList();
    }
}

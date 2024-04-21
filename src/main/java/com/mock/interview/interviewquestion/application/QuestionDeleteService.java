package com.mock.interview.interviewquestion.application;

import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.application.helper.QuestionVerifyHelper;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.QuestionExistsRepository;
import com.mock.interview.questionlike.domain.QuestionLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionDeleteService {
    private final QuestionExistsRepository questionExistsRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final QuestionLikesRepository questionLikesRepository;

    public void delete(long questionId, long loginId) {
        QuestionVerifyHelper.verify(questionExistsRepository, questionId, loginId);

        answerRepository.removeQuestion(questionId);
        interviewConversationPairRepository.removeQuestion(questionId);
        questionLikesRepository.deleteByQuestionId(questionId);
        questionRepository.deleteById(questionId);
    }
}

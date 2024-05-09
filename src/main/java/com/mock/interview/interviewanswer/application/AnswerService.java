package com.mock.interview.interviewanswer.application;

import com.mock.interview.interviewanswer.application.helper.AnswerVerifyHelper;
import com.mock.interview.interviewanswer.domain.InterviewAnswerService;
import com.mock.interview.interviewanswer.domain.exception.InterviewAnswerNotFoundException;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewanswer.infra.AnswerExistsRepository;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForm;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerExistsRepository answerExistsRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewQuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final InterviewAnswerRepository repository;
    private final InterviewAnswerService answerDomainService;

    public long saveAnswer(long questionId, long loginId, AnswerForm form) {
        InterviewQuestion question = questionRepository.findById(questionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);
        Users users = userRepository.findById(loginId)
                .orElseThrow(UserNotFoundException::new);
        InterviewAnswer answer = answerDomainService.createAnswer(question, users, form.getContent());
        return repository.save(answer).getId();
    }

    public void delete(long answerId, long questionId, long loginId) {
        AnswerVerifyHelper.verify(answerExistsRepository, answerId, questionId, loginId);
        conversationPairRepository.removeAnswer(answerId);
        repository.deleteUserAnswer(answerId, loginId);
    }

    public void update(long answerId, long questionId, long loginId, AnswerForm answerForm) {
        InterviewAnswer answer = repository.findUserAnswer(answerId, questionId, loginId)
                .orElseThrow(InterviewAnswerNotFoundException::new);
        answer.updateAnswer(answerForm.getContent());
    }
}

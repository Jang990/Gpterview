package com.mock.interview.questionlike.application;

import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.questionlike.domain.QuestionLike;
import com.mock.interview.questionlike.domain.QuestionLikesRepository;
import com.mock.interview.questionlike.domain.exception.AlreadyLikeQuestionException;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionLikeService {
    private final QuestionLikesRepository questionLikesRepository;
    private final InterviewQuestionRepository questionRepository;
    private final UserRepository userRepository;

    // TODO: 동시성 문제 발생 처리.
    public void like(long userId, long questionId) {
        if(questionLikesRepository.findQuestionLike(userId, questionId).isPresent())
            throw new AlreadyLikeQuestionException();

        Users users = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        InterviewQuestion question = questionRepository.findById(questionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);

        QuestionLike.likeQuestion(questionLikesRepository, users, question);
    }

    public void cancel(long userId, long questionId) {
        QuestionLike questionLike = questionLikesRepository.findQuestionLike(userId, questionId)
                .orElseThrow();
        questionLike.delete();
        questionLikesRepository.delete(questionLike);
    }
}

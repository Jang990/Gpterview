package com.mock.interview.questionlike.application;

import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.questionlike.domain.QuestionLike;
import com.mock.interview.questionlike.domain.QuestionLikesRepository;
import com.mock.interview.questionlike.domain.exception.AlreadyLikeQuestionException;
import com.mock.interview.questionlike.domain.exception.QuestionLikeNotFoundException;
import com.mock.interview.questionlike.infra.lock.QuestionLikeLock;
import com.mock.interview.questionlike.infra.lock.QuestionLikeLockable;
import com.mock.interview.questionlike.presentation.dto.QuestionLikeDto;
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

    @QuestionLikeLock
    public void like(QuestionLikeDto questionLikeDto) {
        if(questionLikesRepository.findQuestionLike(questionLikeDto.getUserId(), questionLikeDto.getQuestionId()).isPresent())
            throw new AlreadyLikeQuestionException();

        Users users = userRepository.findById(questionLikeDto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        InterviewQuestion question = questionRepository.findById(questionLikeDto.getQuestionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);

        QuestionLike.likeQuestion(questionLikesRepository, users, question);
    }

    @QuestionLikeLock
    public void cancel(QuestionLikeDto questionLikeDto) {
        QuestionLike questionLike = questionLikesRepository.findQuestionLike(questionLikeDto.getUserId(), questionLikeDto.getQuestionId())
                .orElseThrow(QuestionLikeNotFoundException::new);
        questionLike.delete();
        questionLikesRepository.delete(questionLike);
    }
}

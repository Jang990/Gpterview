package com.mock.interview.interviewquestion.event;

import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.questionlike.domain.QuestionLikeCanceledEvent;
import com.mock.interview.questionlike.domain.QuestionLikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionLikeEventHandler {
    private final InterviewQuestionRepository repository;

    @EventListener(QuestionLikedEvent.class)
    public void handle(QuestionLikedEvent event) {
        InterviewQuestion question = repository.findById(event.questionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);
        question.like();
    }

    @EventListener(QuestionLikeCanceledEvent.class)
    public void handle(QuestionLikeCanceledEvent event) {
        System.out.println();
        InterviewQuestion question = repository.findById(event.questionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);
        question.cancelLike();
    }
}

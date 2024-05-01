package com.mock.interview.questiontoken.event;

import com.mock.interview.interviewquestion.domain.event.QuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.questiontoken.domain.KoreaStringAnalyzer;
import com.mock.interview.questiontoken.domain.QuestionTokenization;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class QuestionCreationHandler {

    private final InterviewQuestionRepository questionRepository;
    private final KoreaStringAnalyzer koreaStringAnalyzer;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = QuestionCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(QuestionCreatedEvent event) {
        InterviewQuestion question = questionRepository.findById(event.questionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);

        QuestionTokenization tokenization = QuestionTokenization.create(question.getQuestion(), koreaStringAnalyzer);
        question.linkQuestionToken(tokenization);
    }
}

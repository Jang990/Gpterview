package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interview.domain.exception.InterviewNotExpiredException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewanswer.domain.ConversationAnsweredEvent;
import com.mock.interview.interviewanswer.domain.exception.InterviewAnswerNotFoundException;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.InterviewQuestionCreatedEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomInterviewQuestionHandler {
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerRepository answerRepository;

    @EventListener(InterviewQuestionCreatedEvent.class)
    public void handle(InterviewQuestionCreatedEvent event) {
        long interviewId = event.interviewId();
        long questionId = event.questionId();
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotExpiredException::new);
        InterviewQuestion question = interviewQuestionRepository.findById(questionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);

        InterviewConversationPair conversationPair = InterviewConversationPair.create(interview);
        interviewConversationPairRepository.save(conversationPair);
        conversationPair.connectQuestion(question);
    }

    @EventListener(ConversationAnsweredEvent.class)
    public void handle(ConversationAnsweredEvent event) {
        long answerId = event.answerId();
        long pairId = event.pairId();
        InterviewAnswer answer = answerRepository.findById(answerId)
                .orElseThrow(InterviewAnswerNotFoundException::new);
        InterviewConversationPair conversationPair = interviewConversationPairRepository.findById(pairId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        conversationPair.answerQuestion(answer);
    }
}

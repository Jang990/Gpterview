package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interview.domain.exception.InterviewNotExpiredException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewanswer.domain.AnsweredInCustomInterviewEvent;
import com.mock.interview.interviewanswer.domain.exception.InterviewAnswerNotFoundException;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.CreatedCustomInterviewQuestionEvent;
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

    @EventListener(CreatedCustomInterviewQuestionEvent.class)
    public void handle(CreatedCustomInterviewQuestionEvent event) {
        long interviewId = event.interviewId();
        long questionId = event.questionId();
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotExpiredException::new);
        InterviewQuestion question = interviewQuestionRepository.findById(questionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);

        InterviewConversationPair.startConversation(interviewConversationPairRepository, interview, question);
    }

    @EventListener(AnsweredInCustomInterviewEvent.class)
    public void handle(AnsweredInCustomInterviewEvent event) {
        long answerId = event.answerId();
        long pairId = event.pairId();
        InterviewAnswer answer = answerRepository.findById(answerId)
                .orElseThrow(InterviewAnswerNotFoundException::new);
        InterviewConversationPair conversationPair = interviewConversationPairRepository.findById(pairId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        conversationPair.answerQuestion(answer);
    }
}

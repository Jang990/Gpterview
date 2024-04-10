package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interviewanswer.domain.event.ConversationAnsweredEvent;
import com.mock.interview.interviewanswer.domain.exception.InterviewAnswerNotFoundException;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.TopicChangedQuestionCreatedEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomInterviewQuestionHandler {
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerRepository answerRepository;

    @EventListener(ConversationQuestionCreatedEvent.class)
    public void handle(ConversationQuestionCreatedEvent event) {
        InterviewConversationPair conversationPair = interviewConversationPairRepository.findById(event.conversationId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        InterviewQuestion question = interviewQuestionRepository.findById(event.questionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);

        conversationPair.connectQuestionTemp(question);
    }

    @EventListener(TopicChangedQuestionCreatedEvent.class)
    public void handle(TopicChangedQuestionCreatedEvent event) {
        InterviewConversationPair conversationPair = interviewConversationPairRepository.findById(event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        InterviewQuestion question = interviewQuestionRepository.findById(event.questionId())
                .orElseThrow(InterviewQuestionNotFoundException::new);

        conversationPair.changeTopic(question);
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

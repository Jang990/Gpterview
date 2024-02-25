package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.event.CreatedRunningInterviewQuestionEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunningInterviewQuestionHandler {
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    @EventListener(CreatedRunningInterviewQuestionEvent.class)
    public void handle(CreatedRunningInterviewQuestionEvent event) {
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        InterviewQuestion question = interviewQuestionRepository.findById(event.questionId())
                .orElseThrow(InterviewNotFoundException::new);

        InterviewConversationPair conversationPair = InterviewConversationPair.startConversation(interview, question);
        interviewConversationPairRepository.save(conversationPair);
    }
}

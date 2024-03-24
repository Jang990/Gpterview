package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interview.domain.InterviewContinuedEvent;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewContinuedEventHandler {
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewRepository interviewRepository;

    @EventListener(InterviewContinuedEvent.class)
    public void handle(InterviewContinuedEvent event) {
        long interviewId = event.interviewId();
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);

        InterviewConversationPair conversationPair = InterviewConversationPair.create(interview);
        interviewConversationPairRepository.save(conversationPair);
        conversationPair.recommendAiQuestion();
    }
}

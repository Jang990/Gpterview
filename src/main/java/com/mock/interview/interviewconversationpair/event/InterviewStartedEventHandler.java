package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interview.domain.InterviewStartedEvent;
import com.mock.interview.interview.domain.exception.InterviewNotExpiredException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewStartedEventHandler {
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewRepository interviewRepository;

    @EventListener(InterviewStartedEvent.class)
    public void handle(InterviewStartedEvent event) {
        long interviewId = event.interviewId();
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotExpiredException::new);

        InterviewConversationPair conversationPair = InterviewConversationPair.create(interview);
        interviewConversationPairRepository.save(conversationPair);
        conversationPair.start();
    }
}

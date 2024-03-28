package com.mock.interview.interview.event;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.ConversationCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationCompletedEventHandler {
    private final InterviewRepository interviewRepository;

    @EventListener(ConversationCompletedEvent.class)
    public void handle(ConversationCompletedEvent event) {
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        interview.continueInterview();
    }

}

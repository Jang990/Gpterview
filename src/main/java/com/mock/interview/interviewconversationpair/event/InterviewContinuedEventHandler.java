package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interview.domain.event.InterviewContinuedEvent;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.AppearedQuestionIdManager;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewContinuedEventHandler {
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewRepository interviewRepository;
    private final AppearedQuestionIdManager appearedQuestionIdManager;

    @EventListener(InterviewContinuedEvent.class)
    public void handle(InterviewContinuedEvent event) {
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);

        InterviewConversationPair conversationPair = InterviewConversationPair.create(interview);
        interviewConversationPairRepository.save(conversationPair);
        conversationPair.startConversation(appearedQuestionIdManager);
    }
}

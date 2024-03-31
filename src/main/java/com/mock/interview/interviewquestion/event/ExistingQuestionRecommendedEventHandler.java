package com.mock.interview.interviewquestion.event;

import com.mock.interview.interviewconversationpair.domain.event.AnotherQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.event.ExistingQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.QuestionRecommendedService;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExistingQuestionRecommendedEventHandler {

    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final QuestionRecommender questionRecommender;
    private final QuestionRecommendedService questionRecommendedService;

    @EventListener(ExistingQuestionRecommendedEvent.class)
    public void handle(ExistingQuestionRecommendedEvent event) {
        InterviewConversationPair pair = interviewConversationPairRepository.findByIdWithInterviewId(event.pairId(), event.interviewId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        questionRecommendedService.recommendQuestion(questionRecommender, pair);
    }

    @EventListener(AnotherQuestionRecommendedEvent.class)
    public void handle(AnotherQuestionRecommendedEvent event) {
        InterviewConversationPair pair = interviewConversationPairRepository.findByIdWithInterviewId(event.pairId(), event.interviewId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        questionRecommendedService.recommendAnotherQuestion(questionRecommender, pair);
    }
}

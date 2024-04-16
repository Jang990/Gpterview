package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.event.CriticalQuestionSelectionErrorEvent;
import com.mock.interview.interviewquestion.domain.event.QuestionSelectionErrorEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationQuestionExceptionHandler {
    private final InterviewConversationPairRepository repository;
    private final String ERROR_MESSAGE = "오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    private final String CRITICAL_ERROR_MESSAGE = "치명적인 오류가 발생했습니다. 모의 면접을 종료합니다.";

    @EventListener(QuestionSelectionErrorEvent.class)
    public void handle(QuestionSelectionErrorEvent event) {
        InterviewConversationPair conversationPair = repository.findById(event.conversationId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        conversationPair.reset(ERROR_MESSAGE);
    }

    @EventListener(CriticalQuestionSelectionErrorEvent.class)
    public void handle(CriticalQuestionSelectionErrorEvent event) {
        InterviewConversationPair conversationPair = repository.findByIdWithInterviewId(event.conversationId(), event.interviewId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        conversationPair.reset(CRITICAL_ERROR_MESSAGE);
        conversationPair.getInterview().expire();
    }
}

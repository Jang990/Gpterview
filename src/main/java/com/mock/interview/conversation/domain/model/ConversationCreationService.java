package com.mock.interview.conversation.domain.model;

import com.mock.interview.conversation.domain.UserAnsweredEvent;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversationCreationService {
    public InterviewConversation createAnswer(
                Interview interview, MessageDto answer,
                Optional<InterviewConversation> lastConversation
    ) {
            if (interview.isTimeout() && isLastConversationByUser(lastConversation))
                throw new IsAlreadyTimeoutInterviewException();

            InterviewConversation conversation = InterviewConversation.createAnswer(interview, answer);

            if (isInterviewInProgress(interview)) {
                Events.raise(new UserAnsweredEvent(interview.getId()));
            }
            return conversation;
    }

    public void changeTopic(Interview interview, Optional<InterviewConversation> lastConversation) {
        if (interview.isTimeout() && isLastConversationByUser(lastConversation))
            throw new IsAlreadyTimeoutInterviewException();

        lastConversation.get().changeTopic();
    }

    private static boolean isInterviewInProgress(Interview interview) {
        return !interview.isTimeout();
    }

    private boolean isLastConversationByUser(Optional<InterviewConversation> lastConversation) {
        return lastConversation.isEmpty() || lastConversation.get().isUserAnswer();
    }
}

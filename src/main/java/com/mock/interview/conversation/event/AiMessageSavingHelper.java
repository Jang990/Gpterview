package com.mock.interview.conversation.event;

import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.domain.model.Interview;

public class AiMessageSavingHelper {
    static void saveAiMessage(
            InterviewConversationRepository repository,
            ConversationMessageBroker conversationMessageBroker,
            Interview interview, Message message
    ) {
        InterviewConversation interviewConversation = InterviewConversation.createQuestion(interview, message);
        repository.save(interviewConversation);
        conversationMessageBroker.publish(interview.getId(), new MessageDto(interviewConversation.getId(), message.getRole(), message.getContent()));
    }
}

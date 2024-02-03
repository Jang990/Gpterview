package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.global.WebSocketConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationMessageBrokerImpl implements ConversationMessageBroker {


    private final SimpMessageSendingOperations sendingOperations;
    public final String template = WebSocketConfig.BROKER_PREFIX + "/interview/%d";

    @Override
    public void publish(long interviewId, MessageDto message) {
        sendingOperations.convertAndSend(template.formatted(interviewId), message);
    }
}

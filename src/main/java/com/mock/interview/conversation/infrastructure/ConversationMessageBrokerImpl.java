package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.conversation.presentation.dto.QuestionInInterviewDto;
import com.mock.interview.global.WebSocketConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationMessageBrokerImpl implements ConversationMessageBroker {


    private final SimpMessageSendingOperations sendingOperations;
    public final String template = WebSocketConfig.BROKER_PREFIX + "/interview/%d";

    @Override
    public void publish(long interviewId, QuestionInInterviewDto message) {
        log.info("{}번 면접 message({},{}) 발행", interviewId, message.getRole(), message.getContent());
        sendingOperations.convertAndSend(template.formatted(interviewId), message);
    }
}

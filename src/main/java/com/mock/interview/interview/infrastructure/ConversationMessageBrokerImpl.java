package com.mock.interview.interview.infrastructure;

import com.mock.interview.interview.domain.ConversationMessageBroker;
import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interview.presentation.dto.message.QuestionInInterviewDto;
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
    public void publish(long interviewId, long conversationPairId, MessageDto message) {
        QuestionInInterviewDto publishedQuestion = new QuestionInInterviewDto(conversationPairId, message);
        log.info("{}번 면접 {} 메시지 발행", interviewId, publishedQuestion);
        sendingOperations.convertAndSend(template.formatted(interviewId), publishedQuestion);
    }
}

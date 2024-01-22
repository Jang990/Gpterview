package com.mock.interview.conversation.event;

import com.mock.interview.conversation.domain.AiAnsweredEvent;
import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.global.WebSocketConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class AiAnsweredHandler {

    private final ConversationMessageBroker messageBroker;

    @TransactionalEventListener(
            classes = AiAnsweredEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void proceedAiAnswer(AiAnsweredEvent event) {
        // TODO: 메시지 브로커를 통해서 전달할 것.
        messageBroker.publish(String.valueOf(event.interviewId()), event.message());
        System.out.println("메시지 브로커 처리... " + event);
    }
}

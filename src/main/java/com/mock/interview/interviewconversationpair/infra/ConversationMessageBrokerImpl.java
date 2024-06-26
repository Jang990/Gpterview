package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.ConversationMessageBroker;
import com.mock.interview.interview.presentation.dto.message.PublishedQuestionDto;
import com.mock.interview.global.WebSocketConfig;
import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationMessageBrokerImpl implements ConversationMessageBroker {


    private final SimpMessageSendingOperations sendingOperations;
    public final String template = WebSocketConfig.BROKER_PREFIX + "/interview/%d";

    @Override
    public void publish(long interviewId, long conversationPairId, List<InterviewQuestionResponse> questionList) {
        PublishedQuestionDto publishedQuestion = new PublishedQuestionDto(conversationPairId, questionList);
        log.info("{}번 면접 {} 메시지 발행", interviewId, publishedQuestion);
        sendingOperations.convertAndSend(template.formatted(interviewId), publishedQuestion);
    }
}

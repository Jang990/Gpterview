package com.mock.interview.conversation.infrastructure.interview.gpt;

import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interviewquestion.infra.ai.dto.Message;
import com.mock.interview.interviewquestion.infra.ai.gpt.ChatGPTRequester;
import com.mock.interview.interviewquestion.infra.ai.gpt.InterviewAIRequest;
import com.mock.interview.interviewquestion.infra.ai.prompt.AiPrompt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ChatGPTRequesterInSpringTest {

    @Autowired
    ChatGPTRequester requester;

//    @Test
    void test() {
        List<Message> list = List.of(
                new Message(InterviewRole.AI,"안녕하세요. 진행하겠습니다."),
                new Message(InterviewRole.USER, "안녕하세요.")
        );

        AiPrompt mockPrompt = mock(AiPrompt.class);
        when(mockPrompt.getPrompt()).thenReturn("IT 분야 백엔드 직군의 면접을 진행해.");
        InterviewAIRequest request = new InterviewAIRequest(list, mockPrompt);
        requester.sendRequest(request);
    }
}
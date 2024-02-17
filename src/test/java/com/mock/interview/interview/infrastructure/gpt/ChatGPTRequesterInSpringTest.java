package com.mock.interview.interview.infrastructure.gpt;

import com.knuddels.jtokkit.api.Encoding;
import com.mock.interview.conversation.infrastructure.interview.gpt.ChatGPTRequester;
import com.mock.interview.conversation.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.conversation.infrastructure.interview.gpt.OpenAIResponseConvertor;
import com.mock.interview.conversation.infrastructure.interview.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.conversation.infrastructure.interview.gpt.dto.openai.GptFunctionResult;
import com.mock.interview.conversation.infrastructure.interview.gpt.dto.openai.OpenAIMessage;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.conversation.presentation.dto.InterviewRole;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatGPTRequesterInSpringTest {

    @Mock
    RestTemplate openaiRestTemplate;

    @Mock
    OpenAIResponseConvertor convertor;

    @Mock
    Encoding stringTokenCounter;


    @InjectMocks
    ChatGPTRequester requester;

    String prompt = "테스트 프롬프트";

    // TODO: 테스트코드 필요.
    @Test
    @DisplayName("정상처리")
    void test1() {
        List<Message> history = new ArrayList<>();
        InterviewSetting mockSetting = mock(InterviewSetting.class);
        when(mockSetting.getConcept()).thenReturn(prompt);

        ChatGptResponse chatGptResponse = new ChatGptResponse();
        setTestFunctionResponse(chatGptResponse);

        String responseContent = "Hello World";
        when(convertor.convertFunctionResult(any())).thenReturn(new GptFunctionResult(responseContent));
        Message message = requester.sendRequest(new InterviewAIRequest(history, mockSetting));

        Assertions.assertThat(message.getContent()).isEqualTo(responseContent);
        Assertions.assertThat(message.getRole()).isEqualTo(InterviewRole.AI.toString());
    }

    @Test
    @DisplayName("정상처리 - 프롬프트 제한 초과")
    void test2() {
        String prompt = "테스트 프롬프트";
        int promptLimit = 1024;
        List<Message> history = new ArrayList<>();
        InterviewSetting mockSetting = mock(InterviewSetting.class);
        when(mockSetting.getConcept()).thenReturn(prompt);
        when(prompt.length()).thenReturn(promptLimit);

        ChatGptResponse chatGptResponse = new ChatGptResponse();
        setTestFunctionResponse(chatGptResponse);

        String responseContent = "Hello World";
        when(convertor.convertFunctionResult(any())).thenReturn(new GptFunctionResult(responseContent));
        Message message = requester.sendRequest(new InterviewAIRequest(history, mockSetting));

        verify(convertor.convertMessageResult(chatGptResponse));
        Assertions.assertThat(message.getContent()).isEqualTo(responseContent);
        Assertions.assertThat(message.getRole()).isEqualTo(InterviewRole.AI.toString());
    }

    private void setTestFunctionResponse(ChatGptResponse chatGptResponse) {
        List<ChatGptResponse.Choice> list = new ArrayList<>();
        ChatGptResponse.Choice choice = new ChatGptResponse.Choice();
        chatGptResponse.setChoices(list);
        OpenAIMessage testMessage = mock(OpenAIMessage.class);
        choice.setMessage(testMessage);
        list.add(choice);
        when(openaiRestTemplate.postForObject(Mockito.nullable(String.class), any(), any())).thenReturn(chatGptResponse);
    }
}
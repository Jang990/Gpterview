package com.mock.interview.infrastructure.gpt;

import com.mock.interview.interview.interview.gpt.ChatGPTRequester;
import com.mock.interview.interview.interview.gpt.InterviewAIRequest;
import com.mock.interview.interview.interview.gpt.OpenAIResponseConvertor;
import com.mock.interview.interview.interview.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.interview.interview.gpt.dto.openai.GptFunctionResult;
import com.mock.interview.interview.interview.gpt.dto.openai.OpenAIMessage;
import com.mock.interview.interview.interview.setting.InterviewSetting;
import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interview.interview.dto.Message;
import org.assertj.core.api.Assertions;
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
class ChatGPTRequesterTest {

    @Mock
    RestTemplate openaiRestTemplate;

    @Mock
    OpenAIResponseConvertor convertor;

    @InjectMocks
    ChatGPTRequester requester;

    String prompt = "너는 {IT} 분야 {백엔드} 포지션의 면접관. ";

    @Test
    void test1() {
        List<Message> history = initHistory();
        InterviewSetting mockSetting = mock(InterviewSetting.class);
        when(mockSetting.getConcept()).thenReturn(prompt);

        ChatGptResponse chatGptResponse = new ChatGptResponse();
        setTestResponse(chatGptResponse);

        String responseContent = "Hello World";
        when(convertor.convertFunctionResult(any())).thenReturn(new GptFunctionResult(responseContent));
        Message message = requester.sendRequest(new InterviewAIRequest(history, mockSetting));

        Assertions.assertThat(message.getContent()).isEqualTo(responseContent);
        Assertions.assertThat(message.getRole()).isEqualTo(InterviewRole.INTERVIEWER.toString());
    }

    private void setTestResponse(ChatGptResponse chatGptResponse) {
        List<ChatGptResponse.Choice> list = new ArrayList<>();
        ChatGptResponse.Choice choice = new ChatGptResponse.Choice();
        chatGptResponse.setChoices(list);
        OpenAIMessage testMessage = mock(OpenAIMessage.class);
        choice.setMessage(testMessage);
        list.add(choice);
        when(openaiRestTemplate.postForObject(Mockito.nullable(String.class), any(), any())).thenReturn(chatGptResponse);
    }

    private List<Message> initHistory() {
        List<Message> history = new ArrayList<>();
        history.add(new Message(requester.getInterviewerRole(), "Hello User"));
        history.add(new Message(requester.getUserRole(), "Hello Interviewer"));
        return history;
    }
}
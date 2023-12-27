package com.mock.interview.interview.infrastructure.interview.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mock.interview.interview.interview.gpt.OpenAIResponseConvertor;
import com.mock.interview.interview.interview.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.interview.interview.gpt.dto.openai.GptFunctionResult;
import com.mock.interview.interview.interview.gpt.dto.openai.OpenAIMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenAIResponseConvertorTest {

    OpenAIResponseConvertor convertor = new OpenAIResponseConvertor();

    @Test
    @DisplayName("이상한 반환값 정상 변환 시도")
    void test1() throws JsonProcessingException {
        String responseData = "지원자는 데이터베이스 성능을 향상시키기 위해.";

        String[] arr = {
                "{\"response\": \""+responseData+"\"}",
                "{\"response\": \""+responseData+"\" \"reget\": \"\"}",
                "{\"response\": \""+responseData+"\": \"response\": \"abc\"}",
        };


        ChatGptResponse chatGptResponse = new ChatGptResponse();
        OpenAIMessage testResponseMessage = getTestResponseMessage(chatGptResponse);

        for (int i = 0; i < arr.length; i++) {
            when(testResponseMessage.getResponseMessage()).thenReturn(arr[i]);
            GptFunctionResult result = convertor.convertFunctionResult(chatGptResponse);
            Assertions.assertThat(result.getResponse()).isEqualTo(responseData);
        }
    }

    // TODO: 다 지원되도록 수정할 것.
    @Test
    @DisplayName("반환값 변환 실패 목록 - 추후 수정 필요")
    void test2() throws JsonProcessingException {
        String[] arr = {
//                "{\"response\": \"지원자는 데이터베이스\"Hello World!\" 성능을 향상시키기 위해.\"}",
//                "{\"response\": \"지원자는 데이터베이스\"Hello World!\" 성능을 향상시키기 위해.\": \"\"}",
                "{}"
        };

        ChatGptResponse chatGptResponse = new ChatGptResponse();
        OpenAIMessage testResponseMessage = getTestResponseMessage(chatGptResponse);

        for (int i = 0; i < arr.length; i++) {
            when(testResponseMessage.getResponseMessage()).thenReturn(arr[i]);
            GptFunctionResult result = convertor.convertFunctionResult(chatGptResponse);
            System.out.println(result.getResponse());
//            Assertions.assertThat(result.getResponse()).isEqualTo(responseData);
        }
    }

    private OpenAIMessage getTestResponseMessage(ChatGptResponse chatGptResponse) {
        List<ChatGptResponse.Choice> list = new ArrayList<>();
        ChatGptResponse.Choice choice = new ChatGptResponse.Choice();
        chatGptResponse.setChoices(list);
        OpenAIMessage testMessage = mock(OpenAIMessage.class);
        choice.setMessage(testMessage);
        list.add(choice);
        return testMessage;
    }
}
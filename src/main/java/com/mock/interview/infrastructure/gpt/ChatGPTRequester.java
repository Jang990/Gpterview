package com.mock.interview.infrastructure.gpt;

import com.mock.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.infrastructure.gpt.dto.openai.ChatGptRequest;
import com.mock.interview.infrastructure.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.presentaion.web.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTRequester implements AIRequester {

    private final RestTemplate openaiRestTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final String USER_ROLE = "user";
    private final String INTERVIEWER_ROLE = "assistant";
    private final String SYSTEM_ROLE = "system";

    @Override
    public Message sendRequest(InterviewAIRequest request) {
        // TODO: 이 부분에서는 전달하는데 최선을 다할 것. 면접에 대한 관여는 최소한으로 할 것. 면접 관련 부분은 전략에서 다 예외처리를 할 것이다.

        List<Message> messages = request.getHistory();
        setInterviewMode(messages, request.getInterviewSetting());

        ChatGptRequest openAIRequest = new ChatGptRequest(model, messages);
        log.info("요청 : {}", openAIRequest);
        ChatGptResponse response = openaiRestTemplate.postForObject(apiUrl, openAIRequest, ChatGptResponse.class);
        log.info("응답 : {}", response);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("ChatGPT 오류"); // TODO: 커스텀 예외로 바꿀 것
        }

        return response.getChoices().get(0).getMessage();
    }

    private void setInterviewMode(List<Message> messages, InterviewSetting settings) {
        Message systemMsg = new Message(SYSTEM_ROLE, settings.getConcept());
        messages.add(0, systemMsg);
    }

    @Override
    public String getSystemRole() {
        return SYSTEM_ROLE;
    }

    @Override
    public String getUserRole() {
        return USER_ROLE;
    }

    @Override
    public String getInterviewerRole() {
        return INTERVIEWER_ROLE;
    }

    @Override
    public long getMaxToken() {
        return 0;
    }

    @Override
    public boolean isTokenLimitExceeded(List<Message> history) {
        return false;
    }
}

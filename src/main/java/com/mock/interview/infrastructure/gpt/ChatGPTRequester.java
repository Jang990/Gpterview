package com.mock.interview.infrastructure.gpt;

import com.mock.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.presentaion.web.dto.InterviewRole;
import com.mock.interview.infrastructure.gpt.dto.ChatGptRequest;
import com.mock.interview.infrastructure.gpt.dto.ChatGptResponse;
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
        if(containsSystemMessage(request.getHistory()))
            return null; // TODO: 커스텀 Exception 던질 것

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

    private boolean containsSystemMessage(List<Message> history) {
        for (Message message : history) {
            if(message.getRole().equalsIgnoreCase(InterviewRole.SYSTEM.name()))
                return true;
        }

        return false;
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

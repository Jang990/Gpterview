package com.mock.interview.infrastructure.gpt;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.mock.interview.infrastructure.gpt.dto.openai.OpenAIMessage;
import com.mock.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.infrastructure.gpt.dto.openai.ChatGptRequest;
import com.mock.interview.infrastructure.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.presentaion.web.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


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
    private final int LIMIT_TOKEN = 4096;

    @Override
    public Message sendRequest(InterviewAIRequest request) {
        List<OpenAIMessage> history = convertHistory(request.getHistory());
        setInterviewMode(history, request.getInterviewSetting());

        ChatGptRequest openAIRequest = new ChatGptRequest(model, history);
        log.info("요청 : {}", openAIRequest);

        ChatGptResponse response = null;
        try{
            response = openaiRestTemplate.postForObject(apiUrl, openAIRequest, ChatGptResponse.class);
        } catch(HttpClientErrorException e) {
            // MaxToken을 초과했을 가능성이 제일 높음. - HttpClientErrorException$BadRequest
            throw new RuntimeException("서버 오류", e);
        }

        log.info("응답 : {}", response);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("ChatGPT 오류"); // TODO: 커스텀 예외로 바꿀 것
        }
        return convertMessage(response.getChoices().get(0).getMessage());
    }

    private Message convertMessage(OpenAIMessage message) {
        return new Message(message.getRole(), message.getContent());
    }

    private List<OpenAIMessage> convertHistory(List<Message> history) {
        return history.stream()
                .map(msg -> new OpenAIMessage(msg.getRole(), msg.getContent()))
                .collect(Collectors.toList());
    }

    private void setInterviewMode(List<OpenAIMessage> messages, InterviewSetting settings) {
        OpenAIMessage systemMsg = new OpenAIMessage(SYSTEM_ROLE, settings.getConcept());
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
        return LIMIT_TOKEN;
    }

    @Override
    public boolean isTokenLimitExceeded(List<Message> history) {
        // TODO: 여유 토큰을 남겨둬서 답변을 받을 수 있도록 만들어야 함.
        StringBuilder sb = new StringBuilder();
        history.forEach(sb::append);
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);
        int tokens = enc.countTokens(sb.toString());
        return LIMIT_TOKEN <= tokens;
    }
}

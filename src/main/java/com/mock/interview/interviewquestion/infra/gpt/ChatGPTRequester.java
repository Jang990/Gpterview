package com.mock.interview.interviewquestion.infra.gpt;

import com.knuddels.jtokkit.api.Encoding;
import com.mock.interview.interviewquestion.infra.gpt.dto.InterviewAIRequest;
import com.mock.interview.interviewquestion.infra.gpt.dto.Message;
import com.mock.interview.interviewquestion.infra.gpt.dto.openai.ChatGptRequest;
import com.mock.interview.interviewquestion.infra.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.interviewquestion.infra.gpt.dto.openai.OpenAIMessage;
import com.mock.interview.interview.presentation.dto.InterviewRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTRequester implements AISpecification {

    private final RestTemplate openaiRestTemplate;
    private final Encoding stringTokenCounter;
    private final String SIGNATURE = "GPT";

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    public static final String USER_ROLE = "user";
    public static final String INTERVIEWER_ROLE = "assistant";
    public static final String SYSTEM_ROLE = "system";
    private final int LIMIT_TOKEN = 4096;

    public String sendRequest(InterviewAIRequest request) {
        final List<OpenAIMessage> history = convertHistory(request.getHistory());
        ChatGptRequest openAIRequest = ChatGptRequest.create(model, history, request.getAiPrompt());
        ChatGptResponse response = sendRequestToOpenAIServer(openAIRequest);
        String responseMessage = response.getResult();

        log.info("GPT 응답: {}", responseMessage);
        return responseMessage;
    }

    /**
     * AI 서버로 요청을 보냄
     * @param openAIRequest
     * @return
     */
    private ChatGptResponse sendRequestToOpenAIServer(ChatGptRequest openAIRequest) {
        try {
            ChatGptResponse response = openaiRestTemplate.postForObject(apiUrl, openAIRequest, ChatGptResponse.class);
            if (response == null)
                return new ChatGptResponse();
            return response;
        } catch (Exception e) {
            log.error("ChatGPT 오류 발생", e);
            throw e;
        }
    }

    private List<OpenAIMessage> convertHistory(List<Message> history) {
        return history.stream()
                .map(msg -> new OpenAIMessage(convertRole(msg.getRole()), msg.getContent()))
                .collect(Collectors.toList());
    }

    private String convertRole(InterviewRole role) {
        return switch (role) {
            case SYSTEM -> SYSTEM_ROLE;
            case USER -> USER_ROLE;
            case AI -> INTERVIEWER_ROLE;
        };
    }

    @Override
    public String getSignature() {
        return SIGNATURE;
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
    public boolean isTokenLimitExceeded(InterviewAIRequest request) {
        // TODO: 여유 토큰을 남겨둬서 답변을 받을 수 있도록 만들어야 함.
        StringBuilder sb = new StringBuilder();
        sb.append(request.getAiPrompt().getPrompt());
        request.getHistory().forEach(sb::append);
        int tokens = stringTokenCounter.countTokens(sb.toString());
        return LIMIT_TOKEN <= tokens;
    }
}

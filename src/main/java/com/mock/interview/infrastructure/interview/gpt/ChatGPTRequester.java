package com.mock.interview.infrastructure.interview.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.mock.interview.infrastructure.interview.gpt.dto.openai.OpenAIMessage;
import com.mock.interview.infrastructure.interview.gpt.dto.openai.GptFunctionResult;
import com.mock.interview.infrastructure.interview.gpt.dto.openai.ChatGptRequest;
import com.mock.interview.infrastructure.interview.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.infrastructure.interview.dto.Message;
import com.mock.interview.presentaion.web.dto.InterviewRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTRequester implements AIRequester {

    private final RestTemplate openaiRestTemplate;
    private ObjectMapper om = new ObjectMapper();

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
        if(isStartingRequest(request)) {
            request.getHistory().add(0, new Message(USER_ROLE, "네. 준비됐습니다."));
            request.getHistory().add(0, new Message(INTERVIEWER_ROLE, "안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?"));
        }

        List<OpenAIMessage> history = convertHistory(request.getHistory());
        ChatGptRequest openAIRequest = ChatGptRequest.createRequestWithFunction(model, request.getInterviewSetting().getConcept(), history);

        ChatGptResponse response = sendRequestToOpenAIServer(openAIRequest);
        validateResponse(response);

        GptFunctionResult content = convertFunctionResult(response.getFunctionResultString());
        // TODO : "다른 질문 부탁드립니다."로 답변이 올 시에 {} or 빈문자로 응답하는 문제 해결하기.
        log.info("답변: {} - 응답: {}", history.get(history.size()-1).getContent(), content.getResponse());
        return new Message(InterviewRole.INTERVIEWER.toString(), content.getResponse());
    }

    private boolean isStartingRequest(InterviewAIRequest request) {
        return request.getHistory().isEmpty() || request.getHistory().size() < 2; // TODO: 매직넘버 쓰지 말 것
    }

    /**
     * 서버로 받은 Response 검증.
     * response가 null이거나, choices가 null이거나 비어있을 시.
     * GPT를 이용하여 Rule에 맞는 함수 호출로 만든 결과물이 없을 시
     * 예외를 던짐
     * @param response
     */
    private void validateResponse(ChatGptResponse response) {
        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()
                || !StringUtils.hasText(response.getFunctionResultString())
        ) {
            throw new RuntimeException("ChatGPT Response 데이터 누락 오류"); // TODO: 커스텀 예외로 바꿀 것
        }
    }

    /**
     * AI 서버로 요청을 보냄
     * @param openAIRequest
     * @return
     */
    private ChatGptResponse sendRequestToOpenAIServer(ChatGptRequest openAIRequest) {
        try{
//            return openaiRestTemplate.postForObject(apiUrl, openAIRequest, ChatGptResponse.class);
            return openaiRestTemplate.postForObject(apiUrl, openAIRequest, ChatGptResponse.class);
        } catch(HttpClientErrorException e) {
            // MaxToken을 초과했을 가능성이 제일 높음. - HttpClientErrorException$BadRequest
            throw new RuntimeException("BadRequest or AI 서버 오류", e); // TODO: 커스텀 예외로 바꿀 것
        }

    }

    /**
     * Json 형식의 String을 GptFunctionResult 객체로 변환
     * @param messageString "{\n  \"response\": \"안녕하세요\",\n}"
     * @return
     */
    private GptFunctionResult convertFunctionResult(String messageString) {
        if(!messageString.contains("\"response\":"))
            return new GptFunctionResult(messageString);

        try {
            return om.readValue(messageString, GptFunctionResult.class);
        } catch (JsonProcessingException e) {
            log.error("원본-{} : ObjectMapper 변환 오류", messageString, e);
            throw new RuntimeException("ObjectMapper 변환 오류", e); // TODO: 커스텀 예외로 바꿀 것
        }
    }

    private List<OpenAIMessage> convertHistory(List<Message> history) {
        System.out.println(history);
        return history.stream()
                .map(msg -> new OpenAIMessage(msg.getRole(), msg.getContent()))
                .collect(Collectors.toList());
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
        sb.append(request.getInterviewSetting().getConcept());
        request.getHistory().forEach(sb::append);
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);
        int tokens = enc.countTokens(sb.toString());
        return LIMIT_TOKEN <= tokens;
    }
}

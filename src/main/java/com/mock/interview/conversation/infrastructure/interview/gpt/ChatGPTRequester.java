package com.mock.interview.conversation.infrastructure.interview.gpt;

import com.knuddels.jtokkit.api.Encoding;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.gpt.dto.openai.ChatGptRequest;
import com.mock.interview.conversation.infrastructure.interview.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.conversation.infrastructure.interview.gpt.dto.openai.OpenAIMessage;
import com.mock.interview.conversation.presentation.dto.InterviewRole;
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
    private final OpenAIResponseConvertor convertor;
    private final Encoding stringTokenCounter;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final String USER_ROLE = "user";
    private final String INTERVIEWER_ROLE = "assistant";
    private final String SYSTEM_ROLE = "system";
    private final int LIMIT_TOKEN = 4096, FUNCTION_DESCRIPTION_LENGTH_LIMIT = 1024;

    @Override
    public Message sendRequest(InterviewAIRequest request) {
        List<OpenAIMessage> history;
        if (isStartingRequest(request)) {
            history = convertHistory(initStartingMessages());
        } else {
            history = convertHistory(request.getHistory());
        }

        // 경험이 function의 설명으로 들어가면서 너무 길어짐. 그래서 BAD Request가 뜸.
        // history에 마지막 부분에 추가하는 식으로 만들어야 할 수도.
        String responseMessage;
        if (isFunctionDescriptionValid(request.getInterviewSetting().getConcept()))
            responseMessage = sendRequestWithFunction(request, history);
        else
            responseMessage = sendRequestOnlyMessage(request, history);

        log.info("답변: {} - 응답: {}", history.get(history.size()-1).getContent(), responseMessage);
        return new Message(InterviewRole.AI.toString(), responseMessage);
    }

    /** GPT의 Function 기능을 사용하지 않고 메시지 내역에 SYSTEM 프롬프트를 넣어서 보냄. */
    private String sendRequestOnlyMessage(InterviewAIRequest request, List<OpenAIMessage> history) {
        history.add(new OpenAIMessage(SYSTEM_ROLE, request.getInterviewSetting().getConcept()));
        ChatGptRequest openAIRequest = ChatGptRequest.createStartingRequest(model, history);
        ChatGptResponse response = sendRequestToOpenAIServer(openAIRequest);
        return convertor.convertMessageResult(response);
    }

    private String sendRequestWithFunction(InterviewAIRequest request, List<OpenAIMessage> history) {
        ChatGptRequest openAIRequest = ChatGptRequest.createRequestWithFunction(model, request.getInterviewSetting().getConcept(), history);
        ChatGptResponse response = sendRequestToOpenAIServer(openAIRequest);
        return convertor.convertFunctionResult(response).getResponse();
    }

    /** Function Description 필드는 공백 포함 1024자로 제한
     * 참고 : https://community.openai.com/t/function-call-description-max-length/529902 */
    private boolean isFunctionDescriptionValid(String functionDescription) {
        return FUNCTION_DESCRIPTION_LENGTH_LIMIT >= functionDescription.length();
    }

    private List<Message> initStartingMessages() {
        return List.of(
                new Message(USER_ROLE, "네. 준비됐습니다."),
                new Message(INTERVIEWER_ROLE, "안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?")
        );
    }

    private boolean isStartingRequest(InterviewAIRequest request) {
        return request.getHistory().isEmpty() || request.getHistory().size() < 2; // TODO: 매직넘버 쓰지 말 것
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
            log.warn(e.getMessage());
            throw new RuntimeException("BadRequest or AI 서버 오류", e); // TODO: 커스텀 예외로 바꿀 것
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
        int tokens = stringTokenCounter.countTokens(sb.toString());
        return LIMIT_TOKEN <= tokens;
    }
}

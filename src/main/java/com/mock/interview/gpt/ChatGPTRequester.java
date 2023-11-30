package com.mock.interview.gpt;

import com.mock.interview.contorller.dto.InterviewInfo;
import com.mock.interview.gpt.dto.ChatGptRequest;
import com.mock.interview.gpt.dto.ChatGptResponse;
import com.mock.interview.contorller.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTRequester {

    private final RestTemplate openaiRestTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final String INTERVIEWER_ROLE = "system";
    private final String INTERVIEWER_SETTING_FORMAT = "너는 %s 분야 %s 포지션의 면접관이고 user는 면접 지원자야. " +
            "너가 하나씩 질문을 하면 user가 그에 대한 대답을 할꺼야. " +
            "너는 user의 대답에 이상한 부분이 있다면 좀 더 깊게 질문할 수 있어." +
            "기술적인 질문을 많이 해줘" +
            "면접을 시작하자. 바로 질문해줘.";

    public Message sendRequest(InterviewInfo interviewInfo) {
        ChatGptRequest request = new ChatGptRequest(model, interviewInfo.getMessageHistory().getMessages());
        if(isFirstRequest(request) || hasOnlyUserRequest(request))
            setInterviewMode(interviewInfo);

        log.info("요청 : {}", request);
        ChatGptResponse response = openaiRestTemplate.postForObject(apiUrl, request, ChatGptResponse.class);
        log.info("응답 : {}", response);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("ChatGPT 오류");
        }

        return response.getChoices().get(0).getMessage();
    }

    private void setInterviewMode(InterviewInfo interviewInfo) {
        String field = interviewInfo.getProfile().getField();
        String position = interviewInfo.getProfile().getPosition();
        String interviewerSettingMessage = String.format(INTERVIEWER_SETTING_FORMAT, field, position);
        Message interviewerSetting = new Message(INTERVIEWER_ROLE, interviewerSettingMessage);
        interviewInfo.getMessageHistory().getMessages().add(0, interviewerSetting);
    }

    private boolean isFirstRequest(ChatGptRequest request) {
        return request.getMessages().isEmpty();
    }

    private boolean hasOnlyUserRequest(ChatGptRequest request) {
        return !request.getMessages().get(0)
                .getRole().equalsIgnoreCase(INTERVIEWER_ROLE);
    }
}

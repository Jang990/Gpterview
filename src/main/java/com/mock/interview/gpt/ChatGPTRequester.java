package com.mock.interview.gpt;

import com.mock.interview.contorller.dto.InterviewInfo;
import com.mock.interview.contorller.dto.MessageHistory;
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
    private final String INTERVIEWER_SETTING_FORMAT =
            "너는 %s 분야 %s 포지션의 면접관이고 user는 면접 지원자야. " +
            "너는 질문하고 user는 대답한다." +
            "너는 user의 대답에 이상한 부분이 있다면 추궁할 수 있다." +
            "넌 한 번에 응답에 한 번의 질문을 한다.";

    public Message sendRequest(InterviewInfo interviewInfo) {
        if(isFirstMessage(interviewInfo.getMessageHistory()) || hasOnlyUserMessage(interviewInfo.getMessageHistory()))
            setInterviewMode(interviewInfo);

        ChatGptRequest request = new ChatGptRequest(model, interviewInfo.getMessageHistory().getMessages());
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

    private boolean isFirstMessage(MessageHistory history) {
        return history.getMessages().isEmpty();
    }

    private boolean hasOnlyUserMessage(MessageHistory history) {
        return !history.getMessages().get(0)
                .getRole().equalsIgnoreCase(INTERVIEWER_ROLE);
    }
}

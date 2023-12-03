package com.mock.interview.infrastructure.gpt;

import com.mock.interview.presentaion.web.dto.InterviewInfo;
import com.mock.interview.presentaion.web.dto.MessageHistory;
import com.mock.interview.infrastructure.gpt.dto.ChatGptRequest;
import com.mock.interview.infrastructure.gpt.dto.ChatGptResponse;
import com.mock.interview.presentaion.web.dto.Message;
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
            "너는 %s 분야 %s 포지션의 면접관이고 user는 면접 지원자." +
            "너는 질문하고 user는 대답." +
            "너는 user의 대답에 이상한 부분이 있다면 추궁할 수 있다." +
            "추궁할 부분이 없다면 같은 주제로 3번 이상 말하지 말고 주제를 변경"+
            "넌 한 번에 응답에 한 번의 질문.";

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
        // TODO: settings 페이지 개발 완료되는대로 주석 해제할 것.
//        String field = interviewInfo.getProfile().getField();
        String field = "개발";
//        String position = interviewInfo.getProfile().getPosition();
        String position = "백엔드";
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

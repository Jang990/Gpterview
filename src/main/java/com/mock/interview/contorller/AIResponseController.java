package com.mock.interview.contorller;

import com.mock.interview.contorller.dto.InterviewInfo;
import com.mock.interview.gpt.ChatGPTRequester;
import com.mock.interview.contorller.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AIResponseController {

    private final ChatGPTRequester requester;

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
//        MessageHistory history = new MessageHistory(message);
//        ChatResponse chatResponse = requester.sendRequest(history);
//        return chatResponse.getChoices().get(0).getMessage().getContent();
        return "Hello World!";
    }

    @PostMapping("/interview/response")
    public Message chat(@RequestBody InterviewInfo interviewInfo) {
        // TODO: interviewInfo에 system 환경 설정 정보가 없으면 bad Request를 반환할 것.
        return requester.sendRequest(interviewInfo);
    }
}


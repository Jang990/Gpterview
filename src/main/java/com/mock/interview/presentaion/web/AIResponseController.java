package com.mock.interview.presentaion.web;

import com.mock.interview.presentaion.web.dto.InterviewInfo;
import com.mock.interview.infrastructure.gpt.ChatGPTRequester;
import com.mock.interview.presentaion.web.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AIResponseController {

    private final ChatGPTRequester requester;

    @PostMapping("/chat")
    public Message chat(@RequestBody String message) {
//        MessageHistory history = new MessageHistory(message);
//        ChatResponse chatResponse = requester.sendRequest(history);
//        return chatResponse.getChoices().get(0).getMessage().getContent();
        return new Message("assistant", "Hello World!");
    }

    @PostMapping("/interview/response")
    public Message chat(@RequestBody InterviewInfo interviewInfo) {
        System.out.println(interviewInfo);
        // TODO: interviewInfo에 system 환경 설정 정보가 없으면 bad Request를 반환할 것.
        return requester.sendRequest(interviewInfo);
    }
}


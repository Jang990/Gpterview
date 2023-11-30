package com.mock.interview.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyTestController {
    @GetMapping("/interview/start")
    public String talkPage() {
        // TODO: GPT에게 첫 요청을 보내고 modle에 Message InterviewInfo를 설정해줄 것
        return "interview/interview-start";
    }
}

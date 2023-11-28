package com.mock.interview.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyTestController {
    @GetMapping("/interview/start")
    public String talkPage() {
        return "interview/interview-start";
    }
}

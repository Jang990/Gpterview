package com.mock.interview.presentaion.api;

import com.mock.interview.application.AIService;
import com.mock.interview.presentaion.web.dto.InterviewInfo;
import com.mock.interview.presentaion.web.dto.InterviewRole;
import com.mock.interview.presentaion.web.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AIResponseController {

    private final AIService service;

    @PostMapping("/interview/response")
    public ResponseEntity<Message> chat(@RequestBody InterviewInfo interviewInfo) {
        Message response = service.service(interviewInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}


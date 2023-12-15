package com.mock.interview.presentaion.api;

import com.mock.interview.application.AIService;
import com.mock.interview.infrastructure.dto.MessageHistory;
import com.mock.interview.infrastructure.interview.AIObjectConvertor;
import com.mock.interview.presentaion.web.dto.InterviewDTO;
import com.mock.interview.infrastructure.dto.Message;
import com.mock.interview.presentaion.web.dto.MessageDTO;
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
    public ResponseEntity<MessageDTO> chat(@RequestBody InterviewDTO interviewDTO) {
        MessageHistory history = AIObjectConvertor.dtoToObject(interviewDTO.getMessageHistory());
        Message response = service.service(interviewDTO.getInterviewSetting(), history);
        MessageDTO message = new MessageDTO(response.getRole(), response.getContent());
        return new ResponseEntity<>(message, HttpStatus.OK);
//        System.out.println(interviewDTO);
//        return new ResponseEntity<>(new Message("user", "Hello World!"), HttpStatus.OK);
    }

}


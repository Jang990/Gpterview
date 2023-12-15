package com.mock.interview.presentaion.api;

import com.mock.interview.infrastructure.interview.AIService;
import com.mock.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.infrastructure.interview.AIObjectConvertor;
import com.mock.interview.presentaion.web.dto.InterviewRequestDTO;
import com.mock.interview.infrastructure.interview.dto.Message;
import com.mock.interview.presentaion.web.dto.MessageDto;
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
    public ResponseEntity<MessageDto> chat(@RequestBody InterviewRequestDTO interviewRequestDTO) {
        MessageHistory history = AIObjectConvertor.dtoToObject(interviewRequestDTO.getMessageHistory());
        Message response = service.service(interviewRequestDTO.getInterviewSetting(), history);
        MessageDto message = new MessageDto(response.getRole(), response.getContent());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}


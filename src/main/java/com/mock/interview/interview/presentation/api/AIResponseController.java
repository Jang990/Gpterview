package com.mock.interview.interview.presentation.api;

import com.mock.interview.interview.infrastructure.interview.AIService;
import com.mock.interview.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interview.infrastructure.interview.AIObjectConvertor;
import com.mock.interview.interview.presentation.dto.MessageDto;
import com.mock.interview.interview.presentation.dto.InterviewRequestDto;
import com.mock.interview.interview.infrastructure.interview.dto.Message;
import jakarta.validation.Valid;
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
    public ResponseEntity<MessageDto> chat(@Valid @RequestBody InterviewRequestDto interviewRequestDTO) {
        MessageHistory history = AIObjectConvertor.dtoToObject(interviewRequestDTO.getMessageHistory());
        Message response = service.service(interviewRequestDTO.getInterviewSetting(), history);
        MessageDto message = new MessageDto(response.getRole(), response.getContent());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/interview/changing-topic")
    public ResponseEntity<MessageDto> changingTopic(@Valid @RequestBody InterviewRequestDto interviewRequestDTO) {
        MessageHistory history = AIObjectConvertor.dtoToObject(interviewRequestDTO.getMessageHistory());
        Message response = service.changeTopic(interviewRequestDTO.getInterviewSetting(), history);
        MessageDto message = new MessageDto(response.getRole(), response.getContent());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}


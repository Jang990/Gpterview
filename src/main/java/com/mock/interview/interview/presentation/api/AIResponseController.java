package com.mock.interview.interview.presentation.api;

import com.mock.interview.conversation.application.InterviewConversationService;
import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.infrastructure.interview.AIObjectConvertor;
import com.mock.interview.conversation.presentation.dto.InterviewRole;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.presentation.dto.InterviewRequestDto;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AIResponseController {

    private final AIService service;
    private final InterviewService interviewService;
    private final InterviewConversationService conversationService;

    @PostMapping("/interview/response")
    public ResponseEntity<MessageDto> chat(@Valid @RequestBody InterviewRequestDto interviewRequestDTO) {
        MessageHistory history = AIObjectConvertor.dtoToObject(interviewRequestDTO.getMessageHistory());
//        Message response = service.service(interviewRequestDTO.getInterviewSetting(), history);
        MessageDto message = new MessageDto(InterviewRole.INTERVIEWER.toString(), "test");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

//    @PostMapping("/interview/{interviewId}/response")
//    public ResponseEntity<MessageDto> requestAiResponse(
//            @AuthenticationPrincipal(expression = "id") Long loginId,
//            @PathVariable(name = "interviewId") long interviewId,
//            MessageDto message // TODO: Request로 이름 변경
//    ) {
//        conversationService.saveUserAnswer(loginId, interviewId, message);
//        Message response = service.service(loginId, interviewId);
//        MessageDto question = new MessageDto(response.getRole(), response.getContent());
//        conversationService.saveQuestion(loginId, interviewId, question);
//
//        return new ResponseEntity<>(question, HttpStatus.OK);
//    }

    @PostMapping("/interview/changing-topic")
    public ResponseEntity<MessageDto> changingTopic(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        Message response = service.changeTopic(loginId, interviewId);
        MessageDto message = new MessageDto(response.getRole(), response.getContent());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}


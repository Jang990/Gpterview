package com.mock.interview.conversation.presentation.api;

import com.mock.interview.conversation.application.InterviewConversationService;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.application.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterviewConversationController {
    private final AIService service;
    private final InterviewConversationService conversationService;
    @PostMapping("/interview/{interviewId}/conversation/init")
    public ResponseEntity<MessageDto> requestAiResponse(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        Message response = service.service(loginId, interviewId);
        MessageDto question = new MessageDto(response.getRole(), response.getContent());
        conversationService.saveQuestion(loginId, interviewId, question);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @PostMapping("/interview/{interviewId}/conversation/response")
    public ResponseEntity<MessageDto> requestAiResponse(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            MessageDto message // TODO: Request로 이름 변경
    ) {
        conversationService.saveAnswer(loginId, interviewId, message);
        Message response = service.service(loginId, interviewId);
        MessageDto question = new MessageDto(response.getRole(), response.getContent());
        conversationService.saveQuestion(loginId, interviewId, question);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @PostMapping("/interview/{interviewId}/conversation/changing-topic")
    public ResponseEntity<MessageDto> changingTopic(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        Message response = service.changeTopic(loginId, interviewId);
        MessageDto message = new MessageDto(response.getRole(), response.getContent());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

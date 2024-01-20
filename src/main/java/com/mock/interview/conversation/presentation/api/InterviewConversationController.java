package com.mock.interview.conversation.presentation.api;

import com.mock.interview.conversation.application.InterviewConversationService;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterviewConversationController {
    private final AIService service;
    private final InterviewConversationService conversationService;

    @PostMapping("/interview/{interviewId}/conversation/response")
    public ResponseEntity<Void> requestAiResponse(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @RequestBody MessageDto answer // TODO: Request로 이름 변경
    ) {
        System.out.println(answer);
        conversationService.saveUserAnswer(loginId, interviewId, answer);
        return new ResponseEntity<>(HttpStatus.OK);
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

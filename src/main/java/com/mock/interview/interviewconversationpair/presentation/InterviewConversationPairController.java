package com.mock.interview.interviewconversationpair.presentation;

import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interviewconversationpair.application.InterviewConversationPairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterviewConversationPairController {
    private final InterviewConversationPairService conversationPairService;

    @PostMapping("/interview/{interviewId}/conversation/response")
    public ResponseEntity<Void> requestAiResponse(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @RequestBody MessageDto answer // TODO: Request로 이름 변경
    ) {
        System.out.println("InterviewConversationPairController.requestAiResponse");
        System.out.println("loginId = " + loginId + ", interviewId = " + interviewId + ", answer = " + answer);
        conversationPairService.saveUserAnswer(loginId, interviewId, answer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/interview/{interviewId}/conversation/changing-topic")
    public ResponseEntity<Void> changingTopic(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        System.out.println("InterviewConversationPairController.changingTopic");
        System.out.println("loginId = " + loginId + ", interviewId = " + interviewId);
        conversationPairService.changeQuestionTopic(loginId, interviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

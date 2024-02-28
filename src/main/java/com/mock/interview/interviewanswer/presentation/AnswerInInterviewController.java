package com.mock.interview.interviewanswer.presentation;

import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interviewanswer.application.InterviewAnswerInInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerInInterviewController {
    private final InterviewAnswerInInterviewService interviewAnswerInInterviewService;

    @PostMapping("/interview/{interviewId}/question/{questionId}/answer")
    public ResponseEntity<Void> requestAiResponse(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "questionId") long questionId,
            @RequestBody MessageDto answer // TODO: Request로 이름 변경
    ) {
        interviewAnswerInInterviewService.create(loginId, interviewId, questionId, answer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/interview/{interviewId}/question/{questionId}/changing-topic")
    public ResponseEntity<Void> changingTopic(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "questionId") long questionId
    ) {
        interviewAnswerInInterviewService.changeQuestionTopic(loginId, interviewId, questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

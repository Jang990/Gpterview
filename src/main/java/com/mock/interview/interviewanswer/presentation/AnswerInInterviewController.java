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

    // TODO: 대화쌍ID를 넘겨받는게 좋을 것 같다. - 개발 편의성을 고려해서 변경하려면 할 것.
//    @PostMapping("/interview/{interviewId}/question/${pairId}/changing-topic")
    @PostMapping("/interview/{interviewId}/conversation/pair/${pairId}/changing-topic")
    public ResponseEntity<Void> changingTopic(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        interviewAnswerInInterviewService.changeQuestionTopic(loginId, interviewId, pairId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

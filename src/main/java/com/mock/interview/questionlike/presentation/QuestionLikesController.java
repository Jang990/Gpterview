package com.mock.interview.questionlike.presentation;

import com.mock.interview.questionlike.application.QuestionLikeService;
import com.mock.interview.questionlike.presentation.dto.QuestionLikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class QuestionLikesController {
    private final QuestionLikeService likeService;

    @PostMapping("/api/question/{questionId}/like")
    public ResponseEntity<Void> likeQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "questionId") long questionId
    ) {
        likeService.like(new QuestionLikeDto(loginId, questionId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/api/question/{questionId}/like")
    public ResponseEntity<Void> cancelQuestionLike(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "questionId") long questionId
    ) {
        likeService.cancel(new QuestionLikeDto(loginId, questionId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

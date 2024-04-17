package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewquestion.domain.event.CriticalQuestionSelectionErrorEvent;
import com.mock.interview.interviewquestion.domain.event.QuestionSelectionErrorEvent;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
public final class ConversationQuestionExceptionHandlingHelper {
    private ConversationQuestionExceptionHandlingHelper() {}

    public static void handle(Exception exception, long interviewId, long conversationId) {
        if (exception instanceof RuntimeException e) {
            log.info("AI 요청기 RuntimeException 발생", e);
            handleRuntimeException(e, interviewId, conversationId);
            return;
        }

        log.info("AI 요청기 Exception 발생", exception);
        handleException(exception, interviewId, conversationId);
    }

    private static void handleException(Exception e, long interviewId, long conversationId) {
        if(e instanceof NotEnoughQuestion)
            Events.raise(new CriticalQuestionSelectionErrorEvent(e.getClass(), interviewId, conversationId));
        Events.raise(new QuestionSelectionErrorEvent(e.getClass(), interviewId, conversationId));
    }

    private static void handleRuntimeException(RuntimeException e, long interviewId, long conversationId) {
        if (isCriticalException(e)) {
            Events.raise(new CriticalQuestionSelectionErrorEvent(e.getClass(), interviewId, conversationId));
            return;
        }
        Events.raise(new QuestionSelectionErrorEvent(e.getClass(), interviewId, conversationId));
    }

    private static boolean isCriticalException(Exception exception) {
        if(exception instanceof HttpServerErrorException)
            return true;
        if(exception instanceof HttpClientErrorException e)
            return e.getStatusCode() != HttpStatus.TOO_MANY_REQUESTS;
        if(exception instanceof NotEnoughQuestion)
            return true;

        return false;
    }
}

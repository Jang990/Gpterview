package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewquestion.domain.event.CriticalQuestionSelectionErrorEvent;
import com.mock.interview.interviewquestion.domain.event.QuestionSelectionErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.net.SocketTimeoutException;

@Slf4j
public final class ConversationQuestionExceptionHandlingHelper {
    private ConversationQuestionExceptionHandlingHelper() {}

    public static void handle(Throwable throwable, long interviewId, long conversationId) {
        if (throwable instanceof RuntimeException e) {
            log.info("AI 요청기 RuntimeException 발생", e);
            handleRuntimeException(e, interviewId, conversationId);
            return;
        }

        log.info("AI 요청기 Exception 발생", throwable);
        handleException(throwable, interviewId, conversationId);
    }

    private static void handleRuntimeException(RuntimeException e, long interviewId, long conversationId) {
        if (canRetry(e)) {
            Events.raise(new QuestionSelectionErrorEvent(e.getClass(), interviewId, conversationId));
        } else {
            Events.raise(new CriticalQuestionSelectionErrorEvent(e.getClass(), interviewId, conversationId));
        }
    }

    private static void handleException(Throwable throwable, long interviewId, long conversationId) {
        Events.raise(new CriticalQuestionSelectionErrorEvent(throwable.getClass(), interviewId, conversationId));
    }

    private static boolean canRetry(Exception exception) {
        return isTimeoutException(exception)
                || isTooManyRequestException(exception);
    }

    private static boolean isTooManyRequestException(Exception exception) {
        return exception instanceof HttpClientErrorException e && e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS;
    }

    private static boolean isTimeoutException(Exception exception) {
        return exception instanceof ResourceAccessException e && e.getCause() instanceof SocketTimeoutException;
    }
}

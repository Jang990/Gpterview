package com.mock.interview.interview.infra.lock.response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.mock.interview.interview.infra.lock.progress.InterviewProgressLock;

/**
 * 사용되지 않음.
 * 사용자 요청을 처리하는 서비스에 락을 걸어서 중복 요청을 제어하도록 변경함
 * @see InterviewProgressLock
 */
@Deprecated
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AiResponseAwaitLock {
}
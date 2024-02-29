package com.mock.interview.interviewquestion.infra.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 커스텀 인터뷰를 진행하면서 인터뷰 ID에 락을 걸어야 하는 작업을 하는 이벤트 발생 시 사용.
 * 해당 이벤트에 LockableCustomInterviewEvent.class를 구현해야 함.
 * @see LockableCustomInterviewEvent
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AiResponseProcessingLock {
}
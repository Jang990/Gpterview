package com.mock.interview.interview.infra.lock.progress;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인터뷰를 진행 시 락을 걸어야 할 때 사용
 * @see InterviewProgressLockable
 * @see InterviewProgressLockAspect
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterviewProgressLock {
}
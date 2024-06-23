package com.mock.interview.questionlike.infra.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 좋아요 기능에 락을 걸어야 할 때 사용
 * @see QuestionLikeLockable
 * @see QuestionLikeLockAspect
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuestionLikeLock {
}
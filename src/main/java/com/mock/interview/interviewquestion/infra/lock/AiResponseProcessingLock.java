package com.mock.interview.interviewquestion.infra.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: Lockable 인터페이스를 만들어서 getInterviewId()를 구현하게 만들까?
/** 이벤트에서 해당 어노테이션을 사용하길 원하면 getInterviewId()에 이벤트를 추가해서 interviewId를 얻을 수 있도록 만들 것 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AiResponseProcessingLock {
}
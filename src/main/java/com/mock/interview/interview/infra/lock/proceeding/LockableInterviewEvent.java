package com.mock.interview.interview.infra.lock.proceeding;

/**
 * 모의 면접 진행 락을 걸고 싶다면 이벤트에 해당 인터페이스를 구현할 것
 * @see AiResponseAwaitLock
 */
public interface LockableInterviewEvent {
    Long getInterviewId();
}

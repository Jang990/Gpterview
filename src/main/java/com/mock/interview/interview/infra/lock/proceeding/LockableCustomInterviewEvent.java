package com.mock.interview.interview.infra.lock.proceeding;

/**
 * 커스텀 인터뷰를 진행하면서 락을 걸고 싶다면 이벤트에 해당 인터페이스를 구현할 것
 * @see AiResponseProcessingLock
 */
public interface LockableCustomInterviewEvent {
    Long getInterviewId();
}

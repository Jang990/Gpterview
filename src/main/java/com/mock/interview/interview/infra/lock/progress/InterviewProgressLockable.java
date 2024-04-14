package com.mock.interview.interview.infra.lock.progress;

/**
 * 모의 면접 진행 락을 걸고 싶다면 이벤트에 해당 인터페이스를 구현할 것
 * @see InterviewProgressLock
 */
public interface InterviewProgressLockable {
    Long getInterviewId();

    Long getUserId();
}

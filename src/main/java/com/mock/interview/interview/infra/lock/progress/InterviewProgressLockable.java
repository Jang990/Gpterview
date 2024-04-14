package com.mock.interview.interview.infra.lock.progress;

/**
 * 모의 면접 진행 락을 걸고 싶다면 해당 인터페이스를 구현
 * @see InterviewProgressLock
 */
public interface InterviewProgressLockable {
    Long getInterviewId();

    Long getUserId();
}

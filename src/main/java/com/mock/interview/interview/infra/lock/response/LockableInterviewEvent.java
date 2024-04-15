package com.mock.interview.interview.infra.lock.response;

import com.mock.interview.interview.infra.lock.progress.InterviewProgressLockable;

/**
 * 사용되지 않음.
 * 사용자 요청을 처리하는 서비스에 락을 걸어서 중복 요청을 제어하도록 변경함
 * @see InterviewProgressLockable
 */
@Deprecated
public interface LockableInterviewEvent {
    Long getInterviewId();
}

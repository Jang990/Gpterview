package com.mock.interview.questionlike.infra.lock;

/**
 * 좋아요 기능에 락을 걸고 싶다면 해당 인터페이스를 구현한 파라미터 이용
 * @see QuestionLikeLock
 */
public interface QuestionLikeLockable {
    long getQuestionId();
}

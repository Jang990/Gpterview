package com.mock.interview.infrastructure.interview.strategy.stage;

/**
 * 면접 진행 상황에 대한 레코드
 * @param stage 현재 단계
 * @param progress 현재 단계의 진행도   ex) 33.4%, 50%...
 */
public record InterviewProgress(InterviewStage stage, double progress) {
    public InterviewProgress(InterviewStage stage, double progress) {
        if (progress > 100d)
            throw new IllegalArgumentException(); // TODO: 퍼센트만 받도록.

        this.stage = stage;
        this.progress = Math.round(progress);
    }
}

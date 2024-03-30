package com.mock.interview.interviewquestion.infra.ai.progress;

/**
 * 면접 진행 상황에 대한 레코드
 * @param phase 현재 단계
 * @param progress 현재 단계의 진행도   ex) 33.4%, 50%...
 */
public record InterviewProgress(InterviewPhase phase, double progress) {
    public InterviewProgress {
        if (progress < 0d || 1d < progress)
            throw new IllegalArgumentException(); // TODO: 커스텀 예외로 변경
    }
}

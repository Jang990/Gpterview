package com.mock.interview.interview.domain.model;

import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import lombok.Getter;

@Getter
public class InterviewProgress {
    private final InterviewPhase phase;
    private final ProgressPercent progressOfPhase;

    InterviewProgress(InterviewPhase phase, ProgressPercent progressOfPhase) {
        this.phase = phase;
        this.progressOfPhase = progressOfPhase;
    }
}

package com.mock.interview.interview.domain.model;

import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.presentation.dto.InterviewType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InterviewPhases {
    private static final InterviewPhase[] COMPOSITE_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE, InterviewPhase.PERSONAL};
    private static final InterviewPhase[] TECH_EX_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] TECH_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.TECHNICAL};
    private static final InterviewPhase[] EXPERIENCE_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] PERSONAL_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.PERSONAL};

    static InterviewPhase[] getPhaseOrder(InterviewType type) {
        return getOriginalPhase(type).clone();
    }

    private static InterviewPhase[] getOriginalPhase(InterviewType type) {
        return switch (type) {
            case TECHNICAL -> TECH_PHASE_ORDER;
            case PERSONALITY -> PERSONAL_PHASE_ORDER;
            case EXPERIENCE -> EXPERIENCE_PHASE_ORDER;
            case TECHNICAL_EXPERIENCE -> TECH_EX_PHASE_ORDER;
            case COMPOSITE -> COMPOSITE_PHASE_ORDER;
        };
    }

    static int numberOfPhase(InterviewType interviewType) {
        return switch (interviewType) {
            case TECHNICAL, PERSONALITY, EXPERIENCE -> 1;
            case TECHNICAL_EXPERIENCE -> 2;
            case COMPOSITE -> 3;
        };
    }
}

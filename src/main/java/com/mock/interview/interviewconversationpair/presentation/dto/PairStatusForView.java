package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interviewconversationpair.domain.model.PairStatus;

public enum PairStatusForView {
    RECOMMENDING, WAITING_AI, COMPLETED;

    public static PairStatusForView convert(PairStatus status) {
        return switch (status) {
            case RECOMMENDING -> RECOMMENDING;
            case WAITING_AI -> WAITING_AI;
            case COMPLETED -> COMPLETED;
            default -> throw new IllegalArgumentException();
        };
    }
}

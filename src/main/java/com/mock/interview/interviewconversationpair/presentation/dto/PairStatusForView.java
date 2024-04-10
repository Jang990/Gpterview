package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interviewconversationpair.domain.model.PairStatus;

public enum PairStatusForView {
    START, RECOMMENDING, WAITING_AI, WAITING_ANSWER, COMPLETED;

    public static PairStatusForView convert(PairStatus status) {
        return switch (status) {
            case START -> START;
            case RECOMMENDING -> RECOMMENDING;
            case WAITING_AI, CHANGING, WAITING_QUESTION -> WAITING_AI;
            case WAITING_ANSWER -> WAITING_ANSWER;
            case COMPLETED -> COMPLETED;
        };
    }
}

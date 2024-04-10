package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interviewconversationpair.domain.model.PairStatus;

public enum PairStatusForView {
    START, RECOMMENDING, WAITING_AI, WAITING_ANSWER, COMPLETED;

    // TODO: RECOMMENDING 제거할 것
    public static PairStatusForView convert(PairStatus status) {
        return switch (status) {
            case START -> PairStatusForView.START;
            case WAITING_QUESTION -> PairStatusForView.WAITING_AI;
            case WAITING_ANSWER -> PairStatusForView.WAITING_ANSWER;
            case COMPLETED -> PairStatusForView.COMPLETED;
        };
    }
}

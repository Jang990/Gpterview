package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interviewconversationpair.domain.model.PairStatus;

public enum PairStatusForView {
    EMPTY, ONLY_QUESTION, COMPLETED;
    public static PairStatusForView convert(PairStatus status) {
        return switch (status) {
            case START -> throw new IllegalArgumentException("서버 데이터 처리 문제"); // START는 보여질 수 없다.
            case WAITING_QUESTION -> PairStatusForView.EMPTY;
            case WAITING_ANSWER -> PairStatusForView.ONLY_QUESTION;
            case COMPLETED -> PairStatusForView.COMPLETED;
        };
    }
}

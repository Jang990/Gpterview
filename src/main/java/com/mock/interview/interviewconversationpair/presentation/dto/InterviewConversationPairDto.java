package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interviewconversationpair.domain.model.PairStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterviewConversationPairDto {
    private Long id;
    private PairStatusForView status;

    public InterviewConversationPairDto(Long id, PairStatusForView pairStatus) {
        this.id = id;
        this.status = pairStatus;
    }
}

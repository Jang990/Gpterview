package com.mock.interview.interview.presentation.dto;

import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.interviewconversationpair.presentation.dto.PairStatusForView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewStartingDto {
    Long interviewId;
    InterviewConversationPairDto pair;
}

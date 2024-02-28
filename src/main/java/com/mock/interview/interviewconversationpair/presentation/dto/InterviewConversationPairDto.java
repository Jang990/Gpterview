package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.conversation.presentation.dto.MessageDto;
import lombok.Data;

@Data
public class InterviewConversationPairDto {
    private Long pairId;
    private MessageDto question;
    private MessageDto answer;

    public InterviewConversationPairDto(Long pairId, MessageDto question, MessageDto answer) {
        this.pairId = pairId;
        this.question = question;
        this.answer = answer;
    }
}

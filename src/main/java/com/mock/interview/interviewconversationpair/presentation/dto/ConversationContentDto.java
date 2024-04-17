package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;
import lombok.Data;

@Data
public class ConversationContentDto {
    private InterviewConversationPairDto pair;
    private InterviewQuestionResponse question;
    private MessageDto answer;

    public ConversationContentDto(InterviewConversationPairDto pair, InterviewQuestionResponse question, MessageDto answer) {
        this.pair = pair;
        this.question = question;
        this.answer = answer;
    }
}

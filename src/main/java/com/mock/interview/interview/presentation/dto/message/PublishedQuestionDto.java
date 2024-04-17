package com.mock.interview.interview.presentation.dto.message;

import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;

import java.util.List;

public record PublishedQuestionDto(Long conversationPairId, List<InterviewQuestionResponse> question){
}

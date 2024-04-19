package com.mock.interview.interviewquestion.infra.gpt.dto;

import com.mock.interview.interview.presentation.dto.InterviewRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private InterviewRole role;
    private String content;
}

package com.mock.interview.presentaion.web.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequestDTO {
    private InterviewInfoDto interviewSetting;
    @Valid
    private MessageHistoryDto messageHistory;
}

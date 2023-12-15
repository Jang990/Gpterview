package com.mock.interview.presentaion.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {
    private InterviewInfo interviewSetting;
    private MessageHistoryDTO messageHistory;
}

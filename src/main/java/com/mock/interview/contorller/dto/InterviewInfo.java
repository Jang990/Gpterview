package com.mock.interview.contorller.dto;

import lombok.*;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterviewInfo {
    private MessageHistory messageHistory;
    private CandidateProfileDTO profile;
}

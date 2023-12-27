package com.mock.interview.interview.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class InterviewSettingDto {
    private CandidateProfileDto profile;
    private InterviewDetailsDto interviewDetails;
}
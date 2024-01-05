package com.mock.interview.interview.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class InterviewSettingDto {
    private CandidateProfileForm profile;
    private InterviewDetailsDto interviewDetails;
}

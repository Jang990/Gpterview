package com.mock.interview.presentaion.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class InterviewSettingDto {
    private CandidateProfileDto profile;
    private InterviewDetailsDto interviewDetails;
}

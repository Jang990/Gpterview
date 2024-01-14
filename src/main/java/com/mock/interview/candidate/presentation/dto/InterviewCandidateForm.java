package com.mock.interview.candidate.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCandidateForm {
    private CandidateProfileForm profile;
    private InterviewConfigDto interviewDetails;
}

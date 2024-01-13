package com.mock.interview.interview.presentation.dto;

import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSettingDto {
    private CandidateConfigForm profile;
    private InterviewDetailsDto interviewDetails;
}

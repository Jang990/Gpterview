package com.mock.interview.presentaion.web.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class InterviewInfoDto {
    private CandidateProfileDto profile;
    private InterviewDetailsDto interviewDetails;
}

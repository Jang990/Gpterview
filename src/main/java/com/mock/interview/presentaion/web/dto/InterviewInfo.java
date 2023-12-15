package com.mock.interview.presentaion.web.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class InterviewInfo {
    private CandidateProfileDTO profile;
    private InterviewDetailsDTO interviewDetails;
}

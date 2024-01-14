package com.mock.interview.interview.presentation.dto;

import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class InterviewCandidateOverview {
    private long candidateId;
    private String appliedJob;
    private InterviewType type;
    private int durationMinutes;
    private List<TechnicalSubjectsResponse> skills;
    private LocalDateTime createdAt;
}

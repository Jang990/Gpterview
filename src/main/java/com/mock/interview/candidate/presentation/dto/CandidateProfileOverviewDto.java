package com.mock.interview.candidate.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileOverviewDto {
    private long id;
    private String title;
    private String department;  // "개발", "영업", "세무"
    private String field; // BE, FE, 디자인
    private LocalDateTime createdAt;
}

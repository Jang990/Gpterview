package com.mock.interview.presentaion.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인터뷰 세부 설정 사항
 * 인터뷰 진행 타입, 인터뷰 진행 시간
 */
@Data
@NoArgsConstructor
public class InterviewDetailsDTO {
    private InterviewType interviewType;
    private int durationMinutes;
}

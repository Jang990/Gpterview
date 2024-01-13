package com.mock.interview.interview.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인터뷰 세부 설정 사항
 * 인터뷰 진행 타입, 인터뷰 진행 시간
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDetailsDto {
    // TODO: 클래스명을 InterviewConfig로 변경할 것.
    private InterviewType interviewType;
    private int durationMinutes;
}

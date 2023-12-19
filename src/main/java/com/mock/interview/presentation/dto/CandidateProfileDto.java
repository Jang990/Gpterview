package com.mock.interview.presentation.dto;

import lombok.*;

/**
 * 지원자 정보
 */
@Data
@NoArgsConstructor
public class CandidateProfileDto {
    private String department;  // "개발", "영업", "세무"
    private String field; // BE, FE, 디자인
    private String skills; // Java, Spring, Mysql, Jenkins ...
    private String experience;
//    private int yearsOfExperience;   // 경력 연차
//    private String jobDescription; // 채용공고 정보
}

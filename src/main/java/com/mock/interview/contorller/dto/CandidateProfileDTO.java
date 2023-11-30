package com.mock.interview.contorller.dto;

import lombok.*;

/**
 * 지원자 정보
 */
@Data
@NoArgsConstructor
public class CandidateProfileDTO {
    private String field;  // "개발", "영업", "세무"
    private String position; // BE, FE, 디자인
//    private int yearsOfExperience;   // 경력 연차
//    private String skills;     // 보유 기술 목록, 예: "Spring, AWS, JPA, Java"
//    private String jobDescription; // 채용공고 정보
}

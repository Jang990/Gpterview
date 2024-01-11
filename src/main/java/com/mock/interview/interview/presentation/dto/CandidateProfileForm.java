package com.mock.interview.interview.presentation.dto;

import lombok.*;

/**
 * 지원자 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileForm {
    /*
    TODO: 프론트에서 분야추가 and 직무추가를 지원할 것.
    TODO: 디폴트 전략기를 사용할 수 있도록 사용자가 직무를 만드는 방식을 지원해야 한다.
     */
    private Long field; // BE, FE, 디자인
    private String skills; // Java, Spring, Mysql, Jenkins ...
    private String experience;
//    private int yearsOfExperience;   // 경력 연차
//    private String jobDescription; // 채용공고 정보
}

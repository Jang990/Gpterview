package com.mock.interview.presentaion.web.dto;

import lombok.*;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 지원자 정보
 */
@Data
@NoArgsConstructor
public class CandidateProfileDTO {
    private String department;  // "개발", "영업", "세무"
    private String field; // BE, FE, 디자인
    private String skills; // Java, Spring, Mysql, Jenkins ...
//    private int yearsOfExperience;   // 경력 연차
//    private String jobDescription; // 채용공고 정보
}

package com.mock.interview.contorller.dto;

import lombok.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 지원자 정보
 */
@Data
@NoArgsConstructor
public class CandidateProfileDTO {
    private String department;  // "개발", "영업", "세무"
    private String field; // BE, FE, 디자인
    private List<String> skills;
//    private int yearsOfExperience;   // 경력 연차
//    private String jobDescription; // 채용공고 정보

    public CandidateProfileDTO(String department, String field, String skills) {
        this.department = department;
        this.field = field;
        this.skills = new LinkedList<>();
        Collections.addAll(this.skills, skills.split(" "));
    }
}

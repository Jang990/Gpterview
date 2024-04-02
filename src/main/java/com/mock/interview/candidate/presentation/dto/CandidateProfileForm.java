package com.mock.interview.candidate.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 지원자 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileForm {
    private Long categoryId; // IT
    private Long positionId; // BE, FE, 디자인
    private List<String> skills; // Java, Spring, Mysql, Jenkins ...
    private List<@NotBlank String> experiences;
//    private int yearsOfExperience;   // 경력 연차
//    private String jobDescription; // 채용공고 정보

    public void setSkills(String skills) {
        if(skills == null || skills.isBlank())
            this.skills = new ArrayList<>();
        else
            this.skills = Arrays.stream(skills.split(","))
                    .map(String::trim).map(String::toUpperCase)
                    .filter(StringUtils::hasText).toList();
    }
}

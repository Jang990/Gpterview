package com.mock.interview.candidate.presentation.dto;

import com.mock.interview.tech.presentation.dto.TechViewDto;
import jakarta.validation.Valid;
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
    private List<TechViewDto> tech;
    private List<@NotBlank String> experiences;
//    private int yearsOfExperience;   // 경력 연차
//    private String jobDescription; // 채용공고 정보

    public void setTech(String techString) {
        System.out.println(techString);
        this.tech = TechViewDto.convert(techString);
    }
}

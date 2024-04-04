package com.mock.interview.candidate.presentation.dto;

import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * 지원자 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileForm {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JobCategorySelectedIds categories = new JobCategorySelectedIds();
    private List<TechViewDto> tech;
    private List<@NotBlank String> experiences;
//    private int yearsOfExperience;   // 경력 연차
//    private String jobDescription; // 채용공고 정보

    public void setTech(String techString) {
        this.tech = TechViewDto.convert(techString);
    }

    public Long getCategoryId() {return categories.getCategoryId();}
    public Long getPositionId() {return categories.getPositionId();}
    public void setCategoryId(Long categoryId) {categories.setCategoryId(categoryId);}
    public void setPositionId(Long positionId) {this.categories.setPositionId(positionId);}
}

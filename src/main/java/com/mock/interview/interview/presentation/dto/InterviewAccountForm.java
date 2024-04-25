package com.mock.interview.interview.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAccountForm {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JobCategorySelectedIds categories = new JobCategorySelectedIds();

    private List<TechViewDto> tech;

    private List<ExperienceDto> experienceList;

    public Long getCategoryId() {return categories.getCategoryId();}
    public Long getPositionId() {return categories.getPositionId();}

    public void setCategoryId(Long categoryId) {categories.setCategoryId(categoryId);}
    public void setPositionId(Long positionId) {this.categories.setPositionId(positionId);}

    public void setTech(String techString) {
        this.tech = TechViewDto.convert(techString);
    }

    @JsonIgnore
    public List<Long> getExperienceIds() {
        // checkbox를 리스트로 고정하고 받기 떄문에 일부를 체크하지 않으면 experienceDto 내부 값이 null일 수 있음
        return experienceList.stream().filter(experienceDto -> experienceDto.getId() != null)
                .mapToLong(ExperienceDto::getId).boxed().toList();
    }

    @JsonIgnore
    public List<Long> getTechIds() {
        return tech.stream().mapToLong(TechViewDto::getId).boxed().toList();
    }
}

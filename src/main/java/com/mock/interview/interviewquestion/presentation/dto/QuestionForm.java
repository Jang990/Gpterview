package com.mock.interview.interviewquestion.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JobCategorySelectedIds categories = new JobCategorySelectedIds();
    private List<TechViewDto> tech;
    private QuestionTypeForView questionType;
    private String content;
    private Long experienceId;

    public void setTech(List<Long> tech) {
        this.tech = TechViewDto.convert(tech);
    }

    @JsonIgnore
    public List<Long> getTechIds() {
        return tech.stream().mapToLong(TechViewDto::getId).boxed().toList();
    }

    public Long getCategoryId() {return categories.getCategoryId();}
    public Long getPositionId() {return categories.getPositionId();}

    public void setCategoryId(Long categoryId) {categories.setCategoryId(categoryId);}
    public void setPositionId(Long positionId) {this.categories.setPositionId(positionId);}
}

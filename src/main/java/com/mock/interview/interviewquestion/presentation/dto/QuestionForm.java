package com.mock.interview.interviewquestion.presentation.dto;

import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JobCategorySelectedIds categories = new JobCategorySelectedIds();
    private List<String> tech;
    private QuestionTypeForView type;
    private String content;

    public void setTech(String tech) {
        if(tech == null || tech.isBlank())
            this.tech = new ArrayList<>();
        else
            this.tech = Arrays.stream(tech.split(","))
                    .map(String::trim).map(String::toUpperCase)
                    .filter(StringUtils::hasText).toList();
    }

    public Long getCategoryId() {return categories.getCategoryId();}
    public Long getPositionId() {return categories.getPositionId();}

    public void setCategoryId(Long categoryId) {categories.setCategoryId(categoryId);}
    public void setPositionId(Long positionId) {this.categories.setPositionId(positionId);}
}

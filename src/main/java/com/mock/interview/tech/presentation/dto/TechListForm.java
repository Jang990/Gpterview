package com.mock.interview.tech.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechListForm {
    List<TechViewDto> tech = new LinkedList<>();

    public void setTech(List<Long> tech) {
        this.tech = TechViewDto.convert(tech);
    }

    @JsonIgnore
    public List<Long> getTechIds() {
        return tech.stream().mapToLong(TechViewDto::getId).boxed().toList();
    }
}

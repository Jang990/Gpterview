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
public class TechViewDto {
    private long id;
    private String value; // 이름 ex) Java, Jenkins, MySQL ...


    @JsonIgnore
    public String getName() {
        return value;
    }

    public static List<TechViewDto> convert(List<Long> tech) {
        List<TechViewDto> result = new LinkedList<>();
        for (Long id : tech) {
            result.add(new TechViewDto(id, null));
        }
        return result;
    }
}

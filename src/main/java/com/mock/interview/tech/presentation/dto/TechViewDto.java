package com.mock.interview.tech.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mock.interview.global.GlobalConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<TechViewDto> convert(String techString) {
        if(techString == null || techString.isBlank())
            return new ArrayList<>();

        try {
            return Arrays.asList(GlobalConst.om.readValue(techString.getBytes(), TechViewDto[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

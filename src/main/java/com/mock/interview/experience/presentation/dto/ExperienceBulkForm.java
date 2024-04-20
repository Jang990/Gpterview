package com.mock.interview.experience.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceBulkForm {
    List<String> experience = new LinkedList<>();
}

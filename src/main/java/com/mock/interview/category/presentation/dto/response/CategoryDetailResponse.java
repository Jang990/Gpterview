package com.mock.interview.category.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDetailResponse {
    private CategoryResponse department;
    private CategoryResponse field;
}

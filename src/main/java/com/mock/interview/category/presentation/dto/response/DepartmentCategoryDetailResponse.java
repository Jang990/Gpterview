package com.mock.interview.category.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentCategoryDetailResponse {
    private JobCategoryResponse department;
    private JobCategoryResponse field;
}

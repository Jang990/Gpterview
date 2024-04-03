package com.mock.interview.category.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCategorySelectedIds {
    private Long categoryId; // IT
    private Long positionId; // BE, FE, 디자인
}

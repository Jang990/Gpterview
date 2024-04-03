package com.mock.interview.user.presentation.dto;

import com.mock.interview.category.presentation.dto.JobCategoryView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountForm {
    private AccountDto account;
    private JobCategoryView categories;
}

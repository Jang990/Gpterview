package com.mock.interview.user.presentation.dto;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailDto {
    private long userId;
    private String username;
    private String email;
    private String picture;
    private CategoryResponse category;
    private CategoryResponse position;
    private LocalDateTime createdAt;
    private List<TechViewDto> techList;
}

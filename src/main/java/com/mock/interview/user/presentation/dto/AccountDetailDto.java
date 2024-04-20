package com.mock.interview.user.presentation.dto;

import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
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
    private String username;
    private LocalDateTime createdBy;
    private JobCategoryView category;
    private List<TechViewDto> tech;
    private List<ExperienceDto> experienceList;
}

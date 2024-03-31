package com.mock.interview.category.application;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JobCategoryService {
    private final JobCategoryRepository categoryRepository;

    public List<CategoryResponse> findAllCategory() {
        List<JobCategory> allDepartment = categoryRepository.findAll();
        return convert(allDepartment);
    }

    private static List<CategoryResponse> convert(List<JobCategory> allDepartment) {
        return allDepartment.stream()
                .map((department) -> new CategoryResponse(department.getId(), department.getName()))
                .toList();
    }
}

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
        List<JobCategory> allCategory = categoryRepository.findAll();
        return convert(allCategory);
    }

    private static List<CategoryResponse> convert(List<JobCategory> allCategories) {
        return allCategories.stream()
                .map((category) -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }
}

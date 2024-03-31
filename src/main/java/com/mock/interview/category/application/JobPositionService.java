package com.mock.interview.category.application;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.category.presentation.dto.response.CategoryDetailResponse;
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
public class JobPositionService {
    private final JobPositionRepository positionRepository;

    public List<CategoryResponse> findChildPositions(long categoryId) {
        List<JobPosition> allPosition = positionRepository.findChildPositions(categoryId);
        return convertPosition(allPosition);
    }

    public CategoryResponse findCategoryAndField(long fieldId) {
        JobPosition position = positionRepository.findWithCategory(fieldId)
                .orElseThrow(JobCategoryNotFoundException::new);
        return convert(position.getCategory());
    }

    public CategoryResponse findPosition(long positionId) {
        JobPosition position = positionRepository.findById(positionId)
                .orElseThrow(JobCategoryNotFoundException::new);
        return convert(position);
    }

    private CategoryResponse convert(JobCategory category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    private CategoryResponse convert(JobPosition position) {
        return new CategoryResponse(position.getId(), position.getName());
    }

    private CategoryDetailResponse convertPosition(JobPosition field) {
        JobCategory category = field.getCategory();
        return new CategoryDetailResponse(
                new CategoryResponse(category.getId(), category.getName()),
                new CategoryResponse(field.getId(), field.getName())
        );
    }

    private static List<CategoryResponse> convertPosition(List<JobPosition> allCategories) {
        return allCategories.stream()
                .map((category) -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }
}

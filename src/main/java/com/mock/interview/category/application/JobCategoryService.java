package com.mock.interview.category.application;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.presentation.dto.response.DepartmentCategoryDetailResponse;
import com.mock.interview.category.presentation.dto.response.JobCategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JobCategoryService {
    private final JobCategoryRepository repository;

    public List<JobCategoryResponse> findAllDepartment() {
        List<JobCategory> allDepartment = repository.findAllDepartment();
        return convert(allDepartment);
    }

    public List<JobCategoryResponse> findDepartmentField(long departmentId) {
        List<JobCategory> allDepartment = repository.findDepartmentField(departmentId);
        return convert(allDepartment);
    }

    public DepartmentCategoryDetailResponse findDepartmentAndField(long fieldId) {
        JobCategory category = repository.findFieldWithDepartment(fieldId)
                .orElseThrow(JobCategoryNotFoundException::new);
        return convert(category);
    }

    private DepartmentCategoryDetailResponse convert(JobCategory field) {
        JobCategory department = field.getDepartment();
        return new DepartmentCategoryDetailResponse(
                new JobCategoryResponse(department.getId(), department.getName()),
                new JobCategoryResponse(field.getId(), field.getName())
        );
    }

    private static List<JobCategoryResponse> convert(List<JobCategory> allDepartment) {
        return allDepartment.stream()
                .map((department) -> new JobCategoryResponse(department.getId(), department.getName()))
                .toList();
    }
}

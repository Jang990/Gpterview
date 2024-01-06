package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.category.JobCategory;
import com.mock.interview.interview.infrastructure.JobCategoryRepository;
import com.mock.interview.interview.presentation.dto.response.JobCategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
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

    private static List<JobCategoryResponse> convert(List<JobCategory> allDepartment) {
        return allDepartment.stream()
                .map((department) -> new JobCategoryResponse(department.getId(), department.getName()))
                .toList();
    }
}

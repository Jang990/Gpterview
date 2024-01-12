package com.mock.interview.category.presentation.api;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.interview.presentation.dto.response.DepartmentCategoryDetailResponse;
import com.mock.interview.interview.presentation.dto.response.JobCategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job/category")
public class JobCategoryController {
    private final JobCategoryService service;

    @GetMapping("/department")
    public ResponseEntity<List<JobCategoryResponse>> getDepartments() {
        return ResponseEntity.ok(service.findAllDepartment());
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<DepartmentCategoryDetailResponse> getDepartmentAndField(@PathVariable(name = "fieldId") long fieldId) {
        return ResponseEntity.ok(service.findDepartmentAndField(fieldId));
    }

    @GetMapping("/department/{departmentId}/field")
    public ResponseEntity<List<JobCategoryResponse>> getFields(@PathVariable(name = "departmentId") long departmentId) {
        return ResponseEntity.ok(service.findDepartmentField(departmentId));
    }
}

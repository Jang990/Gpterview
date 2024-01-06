package com.mock.interview.interview.presentation.api;

import com.mock.interview.interview.application.JobCategoryService;
import com.mock.interview.interview.presentation.dto.response.JobCategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping("/department/{departmentId}/field")
    public ResponseEntity<List<JobCategoryResponse>> getFields(@PathVariable(name = "departmentId") long departmentId) {
        return ResponseEntity.ok(service.findDepartmentField(departmentId));
    }
}

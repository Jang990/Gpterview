package com.mock.interview.category.presentation.api;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobCategoryController {
    private final JobCategoryService service;

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryResponse>> getDepartments() {
        return ResponseEntity.ok(service.findAllDepartment());
    }
}

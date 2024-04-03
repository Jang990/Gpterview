package com.mock.interview.category.presentation.api;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.presentation.dto.request.NewCategoryRequest;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobCategoryController {
    private final JobCategoryService service;

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(service.findAllCategory());
    }

    @GetMapping("/api/category/{categoryName}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable(name = "categoryName") String categoryName) {
        return ResponseEntity.ok(service.findCategory(categoryName));
    }

    @PostMapping("/api/category")
    public ResponseEntity<Long> save(@RequestBody NewCategoryRequest request) {
        return ResponseEntity.ok(service.save(request));
    }
}

package com.mock.interview.tech.presentation.api;

import com.mock.interview.tech.application.CategoryTechService;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryTechController {
    private final CategoryTechService service;

    @GetMapping("/api/job/category/{categoryId}/tech")
    public ResponseEntity<List<TechnicalSubjectsResponse>> getCategories(@PathVariable(name = "categoryId") long categoryId) {
        return ResponseEntity.ok(service.findRelatedTech(categoryId));
    }
}

package com.mock.interview.category.presentation.api;

import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.dto.response.CategoryDetailResponse;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
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
@RequestMapping("/api/category")
public class JobPositionController {
    private final JobPositionService service;

    @GetMapping("/position/{positionId}")
    public ResponseEntity<CategoryDetailResponse> getDepartmentAndField(@PathVariable(name = "positionId") long positionId) {
        return ResponseEntity.ok(service.findDepartmentAndField(positionId));
    }

    @GetMapping("/{categoryId}/position")
    public ResponseEntity<List<CategoryResponse>> getFields(@PathVariable(name = "categoryId") long categoryId) {
        return ResponseEntity.ok(service.findChildPositions(categoryId));
    }
}

package com.mock.interview.category.presentation.api;

import com.mock.interview.category.application.JobPositionService;
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
@RequestMapping("/api")
public class JobPositionController {
    private final JobPositionService service;

    @GetMapping("/position/{positionId}/category")
    public ResponseEntity<CategoryResponse> getCategoryAndField(@PathVariable(name = "positionId") long positionId) {
        return ResponseEntity.ok(service.findCategoryAndField(positionId));
    }

    @GetMapping("/category/{categoryId}/position")
    public ResponseEntity<List<CategoryResponse>> getFields(@PathVariable(name = "categoryId") long categoryId) {
        return ResponseEntity.ok(service.findChildPositions(categoryId));
    }

    @GetMapping("/position/{positionId}")
    public ResponseEntity<CategoryResponse> getPosition(@PathVariable(name = "positionId") long positionId) {
        return ResponseEntity.ok(service.findPosition(positionId));
    }

//    @PostMapping("/category/{categoryId}/position")
    public ResponseEntity<Long> save(
            @PathVariable(name = "categoryId") long categoryId,
            @RequestBody NewCategoryRequest request
    ) {
        return ResponseEntity.ok(service.save(categoryId, request));
    }
}

package com.mock.interview.tech.presentation.api;

import com.mock.interview.tech.application.TechnicalSubjectsService;
import com.mock.interview.tech.infra.TechnicalSubjectsRepositoryForView;
import com.mock.interview.tech.presentation.dto.TechCreationRequest;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TechController {
    private final TechnicalSubjectsService service;
    private final TechnicalSubjectsRepositoryForView repositoryForView;

    @PostMapping("/api/tech")
    public ResponseEntity<Long> create(@RequestBody TechCreationRequest request) {
        long createdId = service.create(request);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @GetMapping("/api/tech")
    public ResponseEntity<List<TechnicalSubjectsResponse>> findTech(
            @RequestParam(name = "name") String name
    ) {
        return ResponseEntity.ok(repositoryForView.findContainTechs(name, 5));
    }
}
package com.mock.interview.tech.presentation.api;

import com.mock.interview.tech.application.TechnicalSubjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TechController {
    private final TechnicalSubjectsService service;
    @PostMapping("/api/tech")
    public ResponseEntity<Long> create(@RequestBody String techName) {
        long createdId = service.create(techName);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
}
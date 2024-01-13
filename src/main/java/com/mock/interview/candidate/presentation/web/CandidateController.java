package com.mock.interview.candidate.presentation.web;

import com.mock.interview.candidate.application.CandidateConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateConfigService profileService;
}

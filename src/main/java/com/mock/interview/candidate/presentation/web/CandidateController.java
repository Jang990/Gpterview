package com.mock.interview.candidate.presentation.web;

import com.mock.interview.candidate.application.CandidateProfileService;
import com.mock.interview.candidate.presentation.dto.CandidateProfileOverviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateProfileService profileService;
}

package com.mock.interview.interview.presentation.web;

import com.mock.interview.candidate.application.CandidateConfigReadOnlyService;
import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.interview.application.InterviewReadOnlyService;
import com.mock.interview.interview.presentation.dto.InterviewCandidateOverview;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.candidate.application.CandidateConfigService;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.user.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InterviewCandidateController {
}

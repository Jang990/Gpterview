package com.mock.interview.user.presentation.web;

import com.mock.interview.user.application.CandidateProfileService;
import com.mock.interview.user.presentation.dto.CandidateProfileOverviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateProfileService profileService;
    @GetMapping("interview/profile")
    public String loadProfileForInterviewPage(Model model, @AuthenticationPrincipal(expression = "id") Long loginId) {
        model.addAttribute("headerActiveTap", "interview");
        List<CandidateProfileOverviewDto> profileList = profileService.findProfiles(loginId);

//        List<CandidateProfileOverviewDto> profileList = new ArrayList<>();
//        profileList.add(new CandidateProfileOverviewDto(1L, "IT 지원서 1","IT", "백엔드", LocalDateTime.now()));
//        profileList.add(new CandidateProfileOverviewDto(2L, "회계쪽","회계", "회계", LocalDateTime.now()));
//        profileList.add(new CandidateProfileOverviewDto(3L, "IT 지원2","IT", "프론트", LocalDateTime.now()));
        model.addAttribute("candidateProfileOverviewList", profileList);
        return "/profile/for-interview";
    }
}

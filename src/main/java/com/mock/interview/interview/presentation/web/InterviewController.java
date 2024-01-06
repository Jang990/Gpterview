package com.mock.interview.interview.presentation.web;

import com.mock.interview.global.security.form.UsersContext;
import com.mock.interview.interview.application.JobCategoryService;
import com.mock.interview.interview.domain.Category;
import com.mock.interview.interview.presentation.dto.*;
import com.mock.interview.user.application.CandidateProfileService;
import com.mock.interview.user.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InterviewController {

    private final CandidateProfileService candidateProfileService;
    private final JobCategoryService categoryService;

    @GetMapping("/interview/start")
    public String startInterviewPage(
            Model model,
            CandidateProfileForm profile,
            InterviewDetailsDto interviewDetails
    ) {
        // TODO: interviewDetails에 시간으로 Redis로 만료시간 설정할 것.
        InterviewSettingDto interviewSettingDto = new InterviewSettingDto();
        interviewSettingDto.setProfile(profile);
        interviewSettingDto.setInterviewDetails(interviewDetails);
        MessageHistoryDto historyDTO = initHistory();
        model.addAttribute("headerActiveTap", "interview");

        InterviewRequestDto interviewRequestDTO = new InterviewRequestDto(interviewSettingDto, historyDTO);

        model.addAttribute("interviewInfo", interviewRequestDTO);
        return "interview/interview-start";
    }

    private MessageHistoryDto initHistory() {
        MessageHistoryDto historyDTO = new MessageHistoryDto();
        historyDTO.getMessages().add(new MessageDto(InterviewRole.INTERVIEWER.toString(), "안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?"));
        historyDTO.getMessages().add(new MessageDto(InterviewRole.USER.toString(), "네. 준비됐습니다."));
        return historyDTO;
    }

    @GetMapping(value = {"/interview/setting","/interview/setting/{candidateProfileId}"})
    public String speechPage(
            Model model, @AuthenticationPrincipal Users users,
            @PathVariable(required = false, value = "candidateProfileId") Optional<Long> candidateProfileId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        // TODO: Setting DTO를 만들고 Model에 넣어서 타임리프에 th:object로 연결할 것.
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("interviewDetails", new InterviewDetailsDto());

        CandidateProfileForm profileDto = new CandidateProfileForm();
        if (candidateProfileId.isPresent()) {
            validateAuthenticatedUser(users);
            profileDto = candidateProfileService.findProfile(candidateProfileId.get(), users.getId());
        }
        model.addAttribute("candidateProfile", profileDto);

        // TODO: Ajax로 분야에 맞는 필드를 가져오도록 수정해야 한다. 일단 임시로 each문 돌린다.
        // TODO: 지금은 면접 설정은 생략하고 기술면접으로 진행한다. 추후 면접 설정도 추가해야 한다.
        return "interview/interview-setting";
    }

    private void validateAuthenticatedUser(Users users) {
        // TODO: 커스텀 예외로 변경
        if (users == null || users.getId() == null) {
            throw new IllegalAccessError();
        }
    }

    @GetMapping("/interview/setting/")
    public String redirectSettingPage() {
        return "redirect:/interview/setting";
    }
}

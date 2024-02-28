package com.mock.interview.interview.presentation.web;

import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.candidate.application.CandidateConfigService;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationRepositoryForView;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.tech.application.TechnicalSubjectsService;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class InterviewController {

    private final CandidateConfigService candidateConfigService;
    private final InterviewService interviewService;
    private final TechnicalSubjectsService technicalSubjectsService;
    private final InterviewConversationRepositoryForView conversationRepositoryForView;


    @PostMapping("/interview")
    public String startInterviewRequest(
            @Valid CandidateProfileForm profile,
            InterviewConfigDto interviewDetails,
            @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        InterviewCandidateForm interviewCandidateForm = new InterviewCandidateForm(profile, interviewDetails);
        List<TechnicalSubjectsResponse> relationalTech = technicalSubjectsService.saveTech(profile.getSkills());
        long candidateConfigId = candidateConfigService.create(interviewCandidateForm, loginId, relationalTech);
        long interviewId = interviewService.create(loginId, candidateConfigId);
        return "redirect:/interview/" + interviewId;
    }

    @PostMapping("/interview/candidate/{candidateId}")
    public String startInterviewWithConfigRequest(
            @PathVariable(name = "candidateId") long candidateId,
            @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        long interviewId = interviewService.create(loginId, candidateId);
        return "redirect:/interview/" + interviewId;
    }

    @GetMapping("/interview/{interviewId}")
    public String interviewPage(
            Model model,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("interviewId", interviewId);

        Slice<InterviewConversationPairDto> interviewConversations = conversationRepositoryForView.findInterviewConversations(interviewId, loginId, PageRequest.of(0, 25));
        model.addAttribute("messageHistory", interviewConversations);
        if (interviewConversations.hasContent()) {
            List<InterviewConversationPairDto> content = interviewConversations.getContent();
            model.addAttribute("lastConversationPairId", content.get(content.size() - 1).getPairId());
        }
        return "interview/interview-start";
    }
}

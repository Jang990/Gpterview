package com.mock.interview.interview.presentation.web;

import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.category.presentation.CategoryValidator;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.candidate.application.CandidateConfigService;
import com.mock.interview.interview.presentation.dto.InterviewStartingDto;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationRepositoryForView;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.interviewconversationpair.presentation.dto.PairStatusForView;
import com.mock.interview.interviewquestion.application.QuestionRecommendationService;
import com.mock.interview.tech.application.TechnicalSubjectsService;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
    private final QuestionRecommendationService questionRecommendationService;


    @PostMapping("/interview")
    public String startInterviewRequest(
            @Valid CandidateProfileForm profile,
            InterviewConfigDto interviewDetails,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            Model model, BindingResult bindingResult
    ) throws BindException {
        CategoryValidator.validate(bindingResult, new JobCategorySelectedIds(profile.getCategoryId(), profile.getPositionId()));
        InterviewCandidateForm interviewCandidateForm = new InterviewCandidateForm(profile, interviewDetails);
        List<Long> relatedTechIds = profile.getTech().stream()
                .map(TechViewDto::getId).toList();
        long candidateConfigId = candidateConfigService
                .create(interviewCandidateForm, loginId, relatedTechIds);

        InterviewStartingDto interviewStartingDto = interviewService.create(loginId, candidateConfigId);
        model.addAttribute("lastConversationPair", interviewStartingDto.getPair());
        return "redirect:/interview/" + interviewStartingDto.getInterviewId();
    }

    @PostMapping("/interview/candidate/{candidateId}")
    public String startInterviewWithConfigRequest(
            @PathVariable(name = "candidateId") long candidateId,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            Model model
    ) {
        InterviewStartingDto interviewStartingDto = interviewService.create(loginId, candidateId);
        model.addAttribute("lastConversationPair", interviewStartingDto.getPair());
        return "redirect:/interview/" + interviewStartingDto.getInterviewId();
    }

    @GetMapping("/interview/{interviewId}")
    public String interviewPage(
            Model model,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("interviewId", interviewId);

        Slice<ConversationContentDto> interviewConversations = conversationRepositoryForView.findInterviewConversations(interviewId, loginId, PageRequest.of(0, 25));
        model.addAttribute("messageHistory", interviewConversations);
        if (!interviewConversations.hasContent()) {
            return "interview/start";
        }

        List<ConversationContentDto> content = interviewConversations.getContent();
        InterviewConversationPairDto lastConversationPair = content.get(content.size() - 1).getPair();
        model.addAttribute("lastConversationPair", lastConversationPair);
        if (lastConversationPair.getStatus() == PairStatusForView.RECOMMENDING) {
            model.addAttribute("recommendationQuestions",
                    questionRecommendationService.findRecommendation(interviewId, lastConversationPair.getId()));
        }
        return "interview/start";
    }
}

package com.mock.interview.interview.presentation.web;

import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.presentation.dto.InterviewStartingDto;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationRepositoryForView;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.interviewconversationpair.presentation.dto.PairStatusForView;
import com.mock.interview.interviewquestion.application.QuestionRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("isAuthenticated()")
public class InterviewController {

    private final InterviewService interviewService;
    private final InterviewConversationRepositoryForView conversationRepositoryForView;
    private final QuestionRecommendationService questionRecommendationService;


    @PostMapping("/interview")
    public String startInterviewRequest(
            InterviewConfigDto interviewConfigDto,
            @AuthenticationPrincipal(expression = "id") Long loginId, Model model
    ) {
        InterviewStartingDto interviewStartingDto = interviewService.createCustomInterview(loginId, interviewConfigDto);
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

    @GetMapping("/interview/form")
    public String interviewFormPage(
            Model model, @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("candidateConfig", new CandidateProfileForm());
        model.addAttribute("interviewDetails", new InterviewConfigDto());
        return "candidate/form";
    }
}

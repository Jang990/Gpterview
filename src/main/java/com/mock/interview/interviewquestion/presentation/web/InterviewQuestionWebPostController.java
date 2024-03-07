package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interviewquestion.application.QuestionSavingService;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.tech.application.TechnicalSubjectsService;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InterviewQuestionWebPostController {
    private final QuestionSavingService questionSavingService;
    private final TechnicalSubjectsService technicalSubjectsService;
    @PostMapping("question")
    public String save(Model model, QuestionForm form) {
        List<TechnicalSubjectsResponse> relationalTech = technicalSubjectsService.saveTechIfNotExist(form.getTech());
        long savedId = questionSavingService.save(form, relationalTech);
        return "redirect:/question/" + savedId;
    }
}

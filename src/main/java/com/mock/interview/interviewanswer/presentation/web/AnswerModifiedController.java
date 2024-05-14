package com.mock.interview.interviewanswer.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewanswer.application.AnswerService;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForm;
import com.mock.interview.user.presentation.InfoPageInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AnswerModifiedController {
    private final AnswerService answerService;

    @PostMapping("/question/{questionId}/answer/{answerId}/delete")
    public String delete(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable("questionId") long questionId,
            @PathVariable("answerId") long answerId
    ) {
        answerService.delete(answerId, questionId, loginUserDetail.getId());
        return "redirect:/question/"+questionId+"/answer/" + answerId + "/delete/result";
    }


    @PostMapping("/question/{questionId}/answer/{answerId}/edit")
    public String update(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable("questionId") long questionId,
            @PathVariable("answerId") long answerId,
            AnswerForm answerForm
    ) {
        answerService.update(answerId, questionId, loginUserDetail.getId(), answerForm);
        return "redirect:/question/"+questionId+"/answer/" + answerId;
    }

    @GetMapping("/question/{questionId}/answer/{answerId}/delete/result")
    public String result(
            Model model,
            @PathVariable("questionId") long questionId,
            @PathVariable("answerId") long answerId
    ) {
        InfoPageInitializer.initInterviewInfoPage(model,
                "대답 제거 성공",
                "대답을 성공적으로 제거했습니다.",
                "/question/"+ questionId
        );
        return "info/info";
    }

}

package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepositoryForListView;
import com.mock.interview.interviewanswer.presentation.dto.AnswerDetailDto;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForm;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForListView;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.ChildQuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionDetailDto;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.questionlike.domain.LikeExistsRepository;
import com.mock.interview.user.presentation.dto.UnauthorizedPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionDetailWebController {
    private final QuestionRepositoryForView questionRepositoryForView;
    private final QuestionRepositoryForListView questionRepositoryForListView;
    private final InterviewAnswerRepositoryForListView answerRepositoryForView;
    private final LikeExistsRepository likeExistsRepository;

    @GetMapping("/question/{questionId}/unauthorized")
    public String unauthorizedPage(Model model, @PathVariable(name = "questionId") long questionId) {
        model.addAttribute("info", new UnauthorizedPageInfo("접근 권한 없음", String.valueOf(questionId), "/question"));
        return "question/unauthorized";
    }


    @GetMapping("/question/{questionId}")
    public String questionDetailPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(name = "questionId") long questionId
    ) {
        model.addAttribute("headerActiveTap", "interview-question");
        QuestionDetailDto question = questionRepositoryForView.findQuestionDetail(questionId);
        if (QuestionPageInitializer.isUnauthorized(loginUserDetail, question.getQuestion())) {
            return "redirect:/question/"+questionId+"/unauthorized";
        }

        List<AnswerDetailDto> answerTop3 = answerRepositoryForView.findAnswerTop3Likes(questionId);
        List<ChildQuestionOverview> childQuestionTop3 = questionRepositoryForListView.findChildQuestionTop3Likes(questionId);

        if(loginUserDetail == null)
            model.addAttribute("isLike", null);
        else if(likeExistsRepository.isExist(questionId, loginUserDetail.getId()))
            model.addAttribute("isLike", true);
        else
            model.addAttribute("isLike", false);
        QuestionPageInitializer.initQuestionDetail(model, question, answerTop3, childQuestionTop3,loginUserDetail);

        return "question/detail";
    }
}

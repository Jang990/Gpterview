package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InterviewQuestionWebController {
    private final InterviewQuestionRepository questionRepository;

    @GetMapping("question")
    public String questionListPage(Model model) {
        List<QuestionView> list = new ArrayList<>();
        list.add(new QuestionView(1, "ChatGpt", "IT", "백엔드", "운영체제",
                "운영체제에서 프로세스와 스레드의 차이점은 무엇인가요?", LocalDateTime.now(), 1335, 48));
        list.add(new QuestionView(2, "Sim","IT", "백엔드", "DB",
                "MySQL에서 인덱스를 사용하는 이유에 대해 설명해주세요.", LocalDateTime.now(), 71, 2));
        list.add(new QuestionView(3, "Kim", "IT", "백엔드", "MySQL",
                "자료구조 중에서 해시 테이블에 대해 알고 계신가요?", LocalDateTime.now(), 2691, 132));
        list.add(new QuestionView(1, "ChatGpt", "IT", "백엔드", "운영체제",
                "운영체제에서 프로세스와 스레드의 차이점은 무엇인가요?", LocalDateTime.now(), 1335, 48));
        list.add(new QuestionView(2, "Sim","IT", "백엔드", "DB",
                "MySQL에서 인덱스를 사용하는 이유에 대해 설명해주세요.", LocalDateTime.now(), 71, 2));
        list.add(new QuestionView(3, "Kim", "IT", "백엔드", "MySQL",
                "자료구조 중에서 해시 테이블에 대해 알고 계신가요?", LocalDateTime.now(), 2691, 132));

        model.addAttribute("questionList", list);
        return "/question/question-list";
    }

    @Data
    @AllArgsConstructor
    static class QuestionView {
        private long id; // 1
        private String createdBy;

        private String department; // IT
        private String field; // 백엔드
        private String tech; // Redis
        private String content; // Redis를 왜 사용하나요?
        private LocalDateTime createdAt;
        private long hits; // 32
        private long commentCnt; // 3

        // 추천수 비추천수
    }

}

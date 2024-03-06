package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class InterviewQuestionRepositoryForViewTest {
    @Autowired InterviewQuestionRepositoryForView repositoryForView;

//    @Test
    void test() {
        Page<QuestionOverview> result = repositoryForView.findOverviewListWithJobCategory(
                "IT", null, PageRequest.of(0, 30));
        System.out.println(result.getTotalElements()); // 전체 요소 수 90
        System.out.println(result.getTotalPages()); // 전체 페이지 수 3 (2까지 가능)
        System.out.println(result.getNumber()); // 현재 페이지 넘버 0
        System.out.println(result.getNumberOfElements()); // 현재 페이지 요소 수
        for (QuestionOverview questionOverview : result.getContent()) {
            System.out.println("questionOverview = " + questionOverview);
        }
    }
}
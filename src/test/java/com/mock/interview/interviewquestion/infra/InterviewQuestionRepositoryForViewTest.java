package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchOptionsDto;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

//@SpringBootTest
class InterviewQuestionRepositoryForViewTest {
    @Autowired
    QuestionRepositoryForView repositoryForView;

//    @Test
    void test0() {
        QuestionOverview question = repositoryForView.findQuestion(803L);
        System.out.println(question);
    }

//    @Test
    void test1() {
        Assertions.assertThrows(InterviewQuestionNotFoundException.class,
                () -> repositoryForView.findQuestion(0L));
    }

//    @Test
    void test2() {
        QuestionSearchOptionsDto searchCond = QuestionSearchOptionsDto.builder().categoryNameCond("IT").build();
        Page<QuestionOverview> result = repositoryForView.findOverviewList(searchCond, PageRequest.of(0, 30));
        System.out.println(result.getTotalElements()); // 전체 요소 수 90
        System.out.println(result.getTotalPages()); // 전체 페이지 수 3 (2까지 가능)
        System.out.println(result.getNumber()); // 현재 페이지 넘버 0
        System.out.println(result.getNumberOfElements()); // 현재 페이지 요소 수
        for (QuestionOverview questionOverview : result.getContent()) {
            System.out.println("questionOverview = " + questionOverview);
        }
    }
}
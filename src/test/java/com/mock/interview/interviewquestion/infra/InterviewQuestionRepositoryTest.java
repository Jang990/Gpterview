package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//@SpringBootTest
class InterviewQuestionRepositoryTest {
    @Autowired
    InterviewQuestionRepository repository;

//    @Test
    void recommendQueryTest() {
        List<InterviewQuestion> it = repository.findQuestionForRecommend("IT", PageRequest.of(0, 10));
        for (InterviewQuestion question : it) {
            System.out.println(question.getQuestion());
        }
    }

//    @Test
    void test2() {
        System.out.println(repository.countDepartmentQuestion("IT"));
    }
}
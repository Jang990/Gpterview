package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//@SpringBootTest
class InterviewQuestionRepositoryTest {
    @Autowired
    InterviewQuestionRepository repository;

//    @Test
    void test2() {
        System.out.println(repository.countCategoryQuestion("IT"));
    }
}
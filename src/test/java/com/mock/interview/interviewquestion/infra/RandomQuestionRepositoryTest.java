package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.LinkedList;
import java.util.List;

//@SpringBootTest
class RandomQuestionRepositoryTest {
    @Autowired
    RandomQuestionRepository repository;

//    @Test
    void recommendQueryTest() {
        List<InterviewQuestion> it = repository.findTechQuestion(QuestionType.TECHNICAL, 1, 1, List.of(1L),PageRequest.of(0, 10));
        for (InterviewQuestion question : it) {
            System.out.println(question.getQuestion());
        }
    }

//    @Test
    void test2() {
        List<InterviewQuestion> techQuestion = repository.findTechQuestion(QuestionType.TECHNICAL, 1, 1, new LinkedList<>(), PageRequest.of(0, 30));
        for (InterviewQuestion question : techQuestion) {
            System.out.println(question.getId() + " " + question.getQuestion());
        }
    }
}
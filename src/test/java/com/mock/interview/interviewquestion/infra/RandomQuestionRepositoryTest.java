package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//@SpringBootTest
class RandomQuestionRepositoryTest {
    @Autowired
    RandomQuestionRepository repository;

//    @Test
    void recommendQueryTest() {
        List<InterviewQuestion> it = repository.findTechQuestion(QuestionType.TECHNICAL, 1, PageRequest.of(0, 10));
        for (InterviewQuestion question : it) {
            System.out.println(question.getQuestion());
        }
    }
}
package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.presentation.dto.AnswerForView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@SpringBootTest
class InterviewAnswerRepositoryForViewTest {

    @Autowired
    InterviewAnswerRepositoryForListView repository;

//    @Test
    void test() {
        List<AnswerForView> answerTop3Likes = repository.findAnswerTop3Likes(803L);
        for (AnswerForView answerTop3Like : answerTop3Likes) {
            System.out.println("answerTop3Like = " + answerTop3Like);
        }

        List<AnswerForView> myAnswers = repository.findMyAnswer(7L, 803L);
        for (AnswerForView myAnswer : myAnswers) {
            System.out.println("myAnswer = " + myAnswer);
        }
    }

}
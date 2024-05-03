package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.presentation.dto.AnswerDetailDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@SpringBootTest
class InterviewAnswerRepositoryForViewTest {

    @Autowired
    InterviewAnswerRepositoryForListView repository;

//    @Test
    void test() {
        List<AnswerDetailDto> answerTop3Likes = repository.findAnswerTop3Likes(803L);
        for (AnswerDetailDto answerTop3Like : answerTop3Likes) {
            System.out.println("answerTop3Like = " + answerTop3Like);
        }

        List<AnswerDetailDto> myAnswers = repository.findMyAnswer(7L, 803L);
        for (AnswerDetailDto myAnswer : myAnswers) {
            System.out.println("myAnswer = " + myAnswer);
        }
    }

}
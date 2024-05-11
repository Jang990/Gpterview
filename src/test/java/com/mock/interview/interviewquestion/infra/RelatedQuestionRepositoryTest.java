package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class RelatedQuestionRepositoryTest {

    @Autowired
    RelatedQuestionRepository repository;

    final PageRequest pageable = PageRequest.of(0, 30);

//    @Test
    void test1() {
        List<QuestionMetaData> techQuestion = repository.findTechQuestion(1L, 1L, new LinkedList<>(), pageable);
        for (QuestionMetaData metaData : techQuestion) {
            System.out.println(metaData);
        }
        System.out.println(techQuestion.size());
    }

//    @Test
    void test2() {
        List<QuestionMetaData> experienceQuestion = repository.findExperienceQuestion(72L, new LinkedList<>(), pageable);
        System.out.println(experienceQuestion.size());
    }

//    @Test
    void test3() {
        List<QuestionMetaData> personalQuestion = repository.findPersonalQuestion(new LinkedList<>(), pageable);
        System.out.println(personalQuestion.size());
    }

}
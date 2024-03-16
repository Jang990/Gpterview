package com.mock.interview.questiontoken.infra;

import com.mock.interview.questiontoken.domain.QuestionTokenization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class QuestionTokenizationRepositoryTest {

    @Autowired
    QuestionTokenizationRepository repository;
    
//    @Test
    void test() {
        QuestionTokenization questionTokenization = repository.findById(1L).get();
    }
}
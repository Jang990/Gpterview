package com.mock.interview.interviewconversationpair.infra;

import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
class AppearedQuestionIdManagerTest {

    private final long INTERVIEW_ID = 171L;
    @Autowired
    AppearedQuestionIdManagerImpl appearedQuestionIdManagerImpl;

//    @Test
    void test() {
        System.out.println(appearedQuestionIdManagerImpl.find(INTERVIEW_ID));
    }

//    @Test
    void test2() {
        System.out.println(appearedQuestionIdManagerImpl.find(INTERVIEW_ID));
        appearedQuestionIdManagerImpl.appear(INTERVIEW_ID, 1);
        System.out.println(appearedQuestionIdManagerImpl.find(INTERVIEW_ID));
    }

//    @Test
    void test3() {
        appearedQuestionIdManagerImpl.delete(INTERVIEW_ID);
    }

}
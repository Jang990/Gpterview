package com.mock.interview.category.application;

import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
class JobPositionServiceTest {

    @Autowired JobPositionService service;

//    @Test
    void test() {
        System.out.println(service.findChildPositions(1));
        System.out.println(service.findCategoryAndField(12));
    }

}
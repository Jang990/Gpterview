package com.mock.interview.category.application;

import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
class JobCategoryServiceTest {
    @Autowired JobCategoryService service;

//    @Test
    void test() {
        System.out.println(service.findAllCategory());
    }
}
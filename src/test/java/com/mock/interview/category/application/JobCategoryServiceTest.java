package com.mock.interview.category.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class JobCategoryServiceTest {
    @Autowired JobCategoryService service;

//    @Test
    void test() {
        System.out.println(service.findAllDepartment());
    }
}
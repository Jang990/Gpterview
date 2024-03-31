package com.mock.interview.category.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class JobPositionServiceTest {

    @Autowired JobPositionService service;

//    @Test
    void test() {
        System.out.println(service.findChildPositions(1));
        System.out.println(service.findDepartmentAndField(12));
    }

}
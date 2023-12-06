package com.mock.interview.infrastructure.interview;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class CommonInterviewConstGetterTest {

    @Autowired
    CommonInterviewConstGetter constGetter;

//    @Test
    void getSystemRoleFormat() {
        System.out.println(constGetter.getSystemRoleFormat());
    }

//    @Test
    void getUserRoleFormat() {
        System.out.println(constGetter.getUserRoleFormat());
    }

//    @Test
    void getInterviewerRoleFormat() {
        System.out.println(constGetter.getInterviewerRoleFormat());
    }

//    @Test
    void getDepartmentFormat() {
        System.out.println(constGetter.getDepartmentFormat());
    }

//    @Test
    void getFieldFormat() {
        System.out.println(constGetter.getFieldFormat());
    }

//    @Test
    void getSkillsFormat() {
        System.out.println(constGetter.getSkillsFormat());
    }

//    @Test
    void getCommonRule() {
        System.out.println(constGetter.getCommonRule());
    }

//    @Test
    void getCommonProfile() {
        System.out.println(constGetter.getCommonProfile());
    }

//    @Test
    void getCommonSkills() {
        System.out.println(constGetter.getCommonSkills());
    }
}
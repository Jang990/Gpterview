package com.mock.interview.user.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class UserRepositoryForViewTest {
    @Autowired UserRepositoryForView repository;

//    @Test
    void findUserDetail() {
        System.out.println(repository.findUserDetail("test1"));
    }
}
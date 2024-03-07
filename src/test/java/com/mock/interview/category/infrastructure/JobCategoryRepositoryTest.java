package com.mock.interview.category.infrastructure;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class JobCategoryRepositoryTest {

    @Autowired JobCategoryRepository repository;

//    @Test
    void test() {
        JobCategory jobCategory = repository.findByName("IT")
                .orElseThrow(JobCategoryNotFoundException::new);

        System.out.println(jobCategory.getId());
        System.out.println(jobCategory.getName());
    }

}
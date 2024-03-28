package com.mock.interview.category.infra;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import org.springframework.beans.factory.annotation.Autowired;

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
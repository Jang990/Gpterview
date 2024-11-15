package com.mock.interview.interview.infra;

import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.interview.domain.model.Interview;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;


//@SpringBootTest
//@Transactional
class InterviewRepositoryTest {

    @Autowired
    InterviewRepository repository;

    @Autowired
    JobCategoryRepository jcRepository;

    @Autowired


//    @Test
    @DisplayName("limit를 사용하는 exists")
    void test2() {
        System.out.println(repository.existsByIdAndUsersId(1, 8));
    }

}
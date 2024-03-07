package com.mock.interview.interview.infrastructure;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.candidate.infrastructure.CandidateConfigRepository;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.interview.domain.model.Interview;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


//@SpringBootTest
//@Transactional
class InterviewRepositoryTest {

    @Autowired
    InterviewRepository repository;

    @Autowired
    JobCategoryRepository jcRepository;

    @Autowired
    CandidateConfigRepository candidateConfigRepository;

//    @Test
    @DisplayName("패치 조인을 하면 패치조인한 객체는 findById시에 쿼리가 일어나지 않는다.")
    void test() {
        Interview interview = repository.findInterviewSetting(4, 7).get();
        System.out.println(interview);
        CandidateConfig candidateConfig = candidateConfigRepository.findById(interview.getId()).get();
        System.out.println(candidateConfig);
    }

//    @Test
    @DisplayName("limit를 사용하는 exists")
    void test2() {
        System.out.println(repository.existsByIdAndUsersId(1, 8));
    }

}
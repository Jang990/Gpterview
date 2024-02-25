package com.mock.interview.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewQuestionRepository extends JpaRepository<ReviewQuestion, Long> {

}

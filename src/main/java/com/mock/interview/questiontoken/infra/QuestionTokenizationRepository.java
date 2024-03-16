package com.mock.interview.questiontoken.infra;

import com.mock.interview.questiontoken.domain.QuestionTokenization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTokenizationRepository extends JpaRepository<QuestionTokenization, Long> {
}

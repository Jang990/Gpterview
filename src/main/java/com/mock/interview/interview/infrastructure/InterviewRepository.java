package com.mock.interview.interview.infrastructure;

import com.mock.interview.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}

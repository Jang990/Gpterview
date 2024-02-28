package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {
}

package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
}

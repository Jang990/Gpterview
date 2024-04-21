package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {
    @Query("""
            UPDATE InterviewAnswer ia
            SET ia.question = null
            WHERE ia.question.id = :questionId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void removeQuestion(@Param("questionId") long questionId);
}

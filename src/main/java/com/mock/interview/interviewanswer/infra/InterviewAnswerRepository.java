package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {
    @Query("""
            UPDATE InterviewAnswer ia
            SET ia.question = null
            WHERE ia.question.id = :questionId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void removeQuestion(@Param("questionId") long questionId);

    @Query("""
            SELECT ia
            FROM InterviewAnswer ia
            WHERE ia.id = :answerId AND ia.question.id = :questionId AND ia.users.id = :userId
            """)
    Optional<InterviewAnswer> findUserAnswer(@Param("answerId") long answerId, @Param("questionId") long questionId, @Param("userId") long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM InterviewAnswer ia WHERE ia.id = :answerId AND ia.question.id = :questionId AND ia.users.id = :userId")
    int deleteUserAnswer(@Param("answerId") long answerId, @Param("questionId") long questionId, @Param("userId") long userId);
}

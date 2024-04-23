package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    @Query("""
            SELECT COUNT(*)
            FROM InterviewQuestion iq
            LEFT JOIN iq.category c
            INNER JOIN iq.questionToken
            WHERE c.name = :category
            """) // 토큰이 없는 질문은 카운트에서 제외함
    Long countCategoryQuestion(@Param("category") String category);

    @Query("""
            SELECT iq
            FROM InterviewQuestion iq
            WHERE iq.id = :questionId
            """)
    Optional<InterviewQuestion> findOpenQuestion(@Param("questionId") long questionId);

    @Query("""
            SELECT iq
            FROM InterviewQuestion iq
            WHERE iq.id = :questionId AND iq.owner.id = :userId
            """)
    Optional<InterviewQuestion> findUserQuestion(@Param("userId") long userId, @Param("questionId") long questionId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM InterviewQuestion iq WHERE iq.experience.id = :experienceId")
    int deleteByExperienceId(@Param("experienceId") long experienceId);

}

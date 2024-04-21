package com.mock.interview.questionlike.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionLikesRepository extends JpaRepository<QuestionLike, Long> {
    @Query("Select ql From QuestionLike ql Where ql.users.id = :userId AND ql.question.id = :questionId")
    Optional<QuestionLike> findQuestionLike(@Param("userId") long userId, @Param("questionId") long questionId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM QuestionLike ql WHERE ql.question.id = :questionId")
    int deleteByQuestionId(@Param("questionId") long questionId);
}

package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.QuestionTechLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTechLinkRepository extends JpaRepository<QuestionTechLink, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM QuestionTechLink qtl WHERE qtl.question.id = :questionId and qtl.technicalSubjects.id IN :techIds")
    int deleteQuestionTech(@Param("questionId") long questionId, @Param("techIds") List<Long> techIds);
}

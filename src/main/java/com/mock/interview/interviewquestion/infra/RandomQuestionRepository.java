package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RandomQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    /*
    TODO: 다른 방식을 찾아봐야한다.
        1~100에서 90~100이 유효 데이터라면 90이 선택될 확률이 90%이다. 이정도면 랜덤이 아니다.
    */

    String COMMON_RANDOM_QUERY = """
            SELECT iq
            FROM InterviewQuestion iq
            INNER JOIN FETCH iq.questionToken qt
            """;

    @Query(value = COMMON_RANDOM_QUERY + """
            INNER JOIN iq.techLink link ON link.technicalSubjects.id = :techId
            WHERE iq.id NOT IN (:appearedQuestionIds)
                AND iq.questionType = :type
                AND iq.category.id = :categoryId
                AND iq.isHidden = FALSE
            """)
    List<InterviewQuestion> findTechQuestion(@Param("type") QuestionType type, @Param("categoryId") long categoryId, @Param("techId") long techId, @Param("appearedQuestionIds") List<Long> appearedQuestionIds, Pageable pageable);

    @Query(value = COMMON_RANDOM_QUERY + """
            WHERE iq.id NOT IN (:appearedQuestionIds)
                AND iq.questionType = :type
                AND iq.experience.id = :experienceId
                AND iq.isHidden = FALSE
            """)
    List<InterviewQuestion> findExperienceQuestion(@Param("type") QuestionType type, @Param("experienceId") long experienceId , @Param("appearedQuestionIds") List<Long> appearedQuestionIds, Pageable pageable);

    @Query(value = COMMON_RANDOM_QUERY + """
            WHERE iq.id NOT IN (:appearedQuestionIds)
                AND iq.questionType = :type
                AND iq.isHidden = FALSE
            """)
    List<InterviewQuestion> findPersonalQuestion(@Param("type") QuestionType type, @Param("appearedQuestionIds") List<Long> appearedQuestionIds, Pageable pageable);
}

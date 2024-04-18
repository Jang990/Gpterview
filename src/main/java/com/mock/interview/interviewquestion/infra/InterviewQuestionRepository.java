package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    /*
    TODO: 다른 방식을 찾아봐야한다.
        1~100에서 90~100이 유효 데이터라면 90이 선택될 확률이 90%이다. 이정도면 랜덤이 아니다.
     */
    @Query(value = """
            SELECT iq
            FROM InterviewQuestion iq
                JOIN (
                    SELECT CEIL( RAND() * (SELECT MAX(iq2.id) FROM InterviewQuestion iq2) ) as id
                ) random
            INNER JOIN FETCH iq.questionToken qt
            WHERE iq.id >= CAST(random.id as long) AND iq.category.id = :categoryId AND iq.isDeleted = FALSE
            """)
    List<InterviewQuestion> findRandomTechQuestion(@Param("categoryId") long categoryId, Pageable pageable);

    @Query("""
            SELECT COUNT(*)
            FROM InterviewQuestion iq
            LEFT JOIN iq.category c
            INNER JOIN iq.questionToken
            WHERE c.name = :category AND iq.isDeleted = FALSE
            """) // 토큰이 없는 질문은 카운트에서 제외함
    Long countCategoryQuestion(@Param("category") String category);

    @Query("""
            SELECT iq
            FROM InterviewQuestion iq
            WHERE iq.id = :questionId AND iq.isDeleted = FALSE
            """)
    Optional<InterviewQuestion> findOpenQuestion(@Param("questionId") long questionId);

    @Query("""
            SELECT iq
            FROM InterviewQuestion iq
            WHERE iq.id = :questionId AND iq.owner.id = :userId AND iq.isDeleted = FALSE
            """)
    Optional<InterviewQuestion> findUserQuestion(@Param("userId") long userId, @Param("questionId") long questionId);
}

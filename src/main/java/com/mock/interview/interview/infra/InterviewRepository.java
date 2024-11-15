package com.mock.interview.interview.infra;

import com.mock.interview.interview.domain.model.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    @Query("Select iv From Interview iv " +
            "join fetch iv.candidateInfo.category " +
            "join fetch iv.candidateInfo.position " +
            "left join fetch iv.topics.techLink " +
            "left join fetch iv.topics.techLink.technicalSubjects " +
            "Where iv.id = :interviewId And iv.candidateInfo.users.id = :userId")
    Optional<Interview> findInterviewSetting(@Param("interviewId") long interviewId, @Param("userId") long userId);

    @Query("""
            SELECT iv FROM Interview iv
            JOIN FETCH iv.candidateInfo.category
            JOIN FETCH iv.candidateInfo.position
            LEFT JOIN FETCH iv.topics.techLink
            LEFT JOIN FETCH iv.topics.techLink.technicalSubjects
            WHERE iv.id = :interviewId
            """)
    Optional<Interview> findInterviewSetting(@Param("interviewId") long interviewId);

    @Query("Select iv From Interview iv Where iv.id = :interviewId and iv.candidateInfo.users.id = :userId")
    Optional<Interview> findByIdAndUserId(@Param("interviewId") long interviewId, @Param("userId") long userId);

    @Query("Select iv From Interview iv Where iv.timer.expiredAt > current_timestamp")
    Optional<Interview> findActiveInterview(Long loginId);

    @Query(value = """
            SELECT iv
            FROM Interview iv
            WHERE iv.candidateInfo.users.id = :userId
            ORDER BY iv.timer.startedAt DESC
            """, countQuery = "")
    List<Interview> findCurrentInterview(@Param("userId") long loginId, Pageable pageable);
}

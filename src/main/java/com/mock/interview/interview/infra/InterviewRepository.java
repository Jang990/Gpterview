package com.mock.interview.interview.infra;

import com.mock.interview.interview.domain.model.Interview;
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
            "join fetch iv.category " +
            "join fetch iv.position " +
            "left join fetch iv.techLink " +
            "left join fetch iv.techLink.technicalSubjects " +
            "Where iv.id = :interviewId And iv.users.id = :userId")
    Optional<Interview> findInterviewSetting(@Param("interviewId") long interviewId, @Param("userId") long userId);

    @Query("""
            SELECT iv FROM Interview iv
            JOIN FETCH iv.category
            JOIN FETCH iv.position
            LEFT JOIN FETCH iv.techLink
            LEFT JOIN FETCH iv.techLink.technicalSubjects
            WHERE iv.id = :interviewId
            """)
    Optional<Interview> findInterviewSetting(@Param("interviewId") long interviewId);

    @Query("Select iv From Interview iv Where iv.id = :interviewId and iv.users.id = :userId")
    Optional<Interview> findByIdAndUserId(@Param("interviewId") long interviewId, @Param("userId") long userId);

    @Query("Select iv From Interview iv Where iv.expiredTime > current_timestamp")
    Optional<Interview> findActiveInterview(Long loginId);

    @Query("""
            Select iv From Interview iv
            Where iv.users.id = :userId
            ORDER BY iv.createdAt DESC
            """)
    Optional<Interview> findCurrentInterview(@Param("userId") long loginId, Pageable pageable);

    /** 인터뷰 소유자 검증을 위한 쿼리 메소드 */
    boolean existsByIdAndUsersId(long interviewId, long usersId);
}

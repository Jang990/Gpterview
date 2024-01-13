package com.mock.interview.interview.infrastructure;

import com.mock.interview.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    @Query("Select iv From Interview iv " +
            "join fetch iv.candidateProfile " +
            "left join fetch iv.candidateProfile.techLink " +
            "left join fetch iv.candidateProfile.techLink.technicalSubjects " +
            "Where iv.id = :interviewId And iv.users.id = :userId")
    Optional<Interview> findInterviewSetting(@Param("interviewId") long interviewId, @Param("userId") long userId);

    @Query("Select iv From Interview iv Where iv.id = :interviewId and iv.users.id = :userId")
    Optional<Interview> findByIdWitUserId(@Param("interviewId") long interviewId, @Param("userId") long userId);

    @Query("Select iv From Interview iv " +
            "join fetch iv.candidateProfile " +
            "left join fetch iv.candidateProfile.techLink " +
            "left join fetch iv.candidateProfile.techLink.technicalSubjects " +
            "Where iv.users.id = :userId")
    List<Interview> findUserInterviewWithProfileAndTech(@Param("userId") long userId);
}

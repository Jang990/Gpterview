package com.mock.interview.interview.infrastructure;

import com.mock.interview.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    @Query("Select iv From Interview iv " +
            "join fetch iv.candidateProfile " +
            "join fetch iv.candidateProfile.jobLink " +
            "join fetch iv.candidateProfile.jobLink.jobCategory " +
            "left join fetch iv.candidateProfile.techLink " +
            "join fetch iv.candidateProfile.techLink.technicalSubjects " +
            "Where iv.id = :profileId and iv.users.id = :userId")
    Optional<Interview> findInterviewSetting(@Param("profileId") long interviewId, @Param("userId") long userId);

    @Query("Select iv From Interview iv Where iv.id = :profileId and iv.users.id = :userId")
    Optional<Interview> findByIdWitUserId(@Param("profileId") long interviewId, @Param("userId") long userId);
}

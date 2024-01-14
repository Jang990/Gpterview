package com.mock.interview.candidate.infrastructure;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateConfigRepository extends JpaRepository<CandidateConfig, Long> {
    @Query("Select cc From CandidateConfig cc Where cc.users.id = :userId")
    List<CandidateConfig> findByUserId(@Param("userId") long userId);

    @Query("Select cc From CandidateConfig cc Where cc.id = :profileId and cc.users.id = :userId")
    Optional<CandidateConfig> findByIdAndUserId(@Param("profileId") long profileId, @Param("userId") long userId);

    @Query("Select cc From CandidateConfig cc " +
            "join fetch cc.appliedJob " +
            "join fetch cc.appliedJob.department " +
            "left join fetch cc.techLink " +
            "left join fetch cc.techLink.technicalSubjects " +
            "Where cc.users.id = :userId")
    List<CandidateConfig> findInterviewConfigByUserId(@Param("userId") long userId);

    @Query("Select cc From CandidateConfig cc " +
            "join fetch cc.appliedJob " +
            "join fetch cc.appliedJob.department " +
            "left join fetch cc.techLink " +
            "left join fetch cc.techLink.technicalSubjects " +
            "Where cc.id = :candidateId and cc.users.id = :userId")
    Optional<CandidateConfig> findInterviewConfig(@Param("candidateId") long candidateId, @Param("userId") long userId);
}

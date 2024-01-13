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
    @Query("Select cp From CandidateConfig cp Where cp.users.id = :userId")
    List<CandidateConfig> findByUserId(@Param("userId") long userId);

    @Query("Select cp From CandidateConfig cp Where cp.id = :profileId and cp.users.id = :userId")
    Optional<CandidateConfig> findByIdWitUserId(@Param("profileId") long profileId, @Param("userId") long userId);
}

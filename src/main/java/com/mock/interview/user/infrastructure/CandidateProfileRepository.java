package com.mock.interview.user.infrastructure;

import com.mock.interview.user.domain.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
    @Query("Select cp From CandidateProfile cp Where cp.users.id = :userId")
    List<CandidateProfile> findByUserId(@Param("userId") long userId);

    @Query("Select cp From CandidateProfile cp Where cp.id = :profileId and cp.users.id = :userId")
    Optional<CandidateProfile> findByIdWitUserId(@Param("profileId") long profileId, @Param("userId") long userId);
}

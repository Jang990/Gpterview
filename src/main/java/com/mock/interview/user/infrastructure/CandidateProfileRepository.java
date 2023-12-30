package com.mock.interview.user.infrastructure;

import com.mock.interview.user.domain.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
    @Query("Select cp From CandidateProfile cp Where cp.users.id = :userId")
    List<CandidateProfile> findByUserId(@Param("userId") long userId);
}

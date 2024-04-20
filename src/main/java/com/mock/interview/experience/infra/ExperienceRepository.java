package com.mock.interview.experience.infra;

import com.mock.interview.experience.domain.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    @Query("Select e From Experience e Where e.id = :experienceId and e.users.id = :userId")
    Optional<Experience> findByIdAndUserId(@Param("experienceId") long experienceId, @Param("userId") long userId);
}

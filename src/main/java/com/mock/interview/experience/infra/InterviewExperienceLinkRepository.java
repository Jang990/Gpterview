package com.mock.interview.experience.infra;

import com.mock.interview.interview.domain.model.InterviewExperienceLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewExperienceLinkRepository extends JpaRepository<InterviewExperienceLink, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM InterviewExperienceLink iel WHERE iel.experience.id = :experienceId")
    int deleteByExperienceId(@Param("experienceId") long experienceId);
}

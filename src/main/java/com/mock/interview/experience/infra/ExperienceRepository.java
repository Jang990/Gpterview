package com.mock.interview.experience.infra;

import com.mock.interview.experience.domain.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}

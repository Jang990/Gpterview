package com.mock.interview.experience.infra;

import com.mock.interview.user.domain.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}

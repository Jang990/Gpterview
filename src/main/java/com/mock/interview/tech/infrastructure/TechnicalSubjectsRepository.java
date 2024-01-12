package com.mock.interview.tech.infrastructure;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechnicalSubjectsRepository extends JpaRepository<TechnicalSubjects, Long> {
    @Query("Select ts From TechnicalSubjects ts Where ts.name in :keywords")
    List<TechnicalSubjects> findTech(@Param("keywords") List<String> keywords);
}

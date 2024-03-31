package com.mock.interview.category.infra;

import com.mock.interview.category.domain.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {

    // TODO: 제거할 것
    @Query("Select jc From JobCategory jc join jc.category Where jc.id = :positionId")
    Optional<JobCategory> findFieldWithCategory(@Param("positionId") long positionId);

    Optional<JobCategory> findByName(String name);
}

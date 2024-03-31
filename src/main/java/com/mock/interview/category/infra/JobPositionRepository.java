package com.mock.interview.category.infra;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {
    @Query("Select jp From JobPosition jp Where jp.category.id = :categoryId")
    List<JobPosition> findChildPositions(@Param("categoryId") long categoryId);

    @Query("Select jp From JobPosition jp join fetch jp.category Where jp.id = :positionId")
    Optional<JobPosition> findWithCategory(@Param("positionId") long positionId);
}

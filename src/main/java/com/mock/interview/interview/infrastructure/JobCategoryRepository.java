package com.mock.interview.interview.infrastructure;

import com.mock.interview.interview.domain.category.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    @Query("Select jc From JobCategory jc Where jc.department is null")
    List<JobCategory> findAllDepartment();

    @Query("Select jc From JobCategory jc Where jc.department.id = :departmentId")
    List<JobCategory> findDepartmentField(@Param("departmentId") long departmentId);

    @Query("Select jc From JobCategory jc join fetch jc.department Where jc.id = :fieldId")
    Optional<JobCategory> findFieldWithDepartment(@Param("fieldId") long fieldId);

    @Query("Select jc From JobCategory jc " +
            "Join ProfileJobCategoryLink pjc On pjc.jobCategory.id = jc.id " +
            "Where pjc.profile.id = :profileId")
    List<JobCategory> findInterviewCategory(@Param("profileId") long profileId);
}

package com.mock.interview.user.infrastructure;

import com.mock.interview.user.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    @Query("""
            SELECT u FROM Users u
            JOIN FETCH u.category
            JOIN FETCH u.position
            LEFT JOIN FETCH u.techLink
            LEFT JOIN FETCH u.techLink.technicalSubjects
            WHERE u.id = :userId
            """)
    Optional<Users> findForInterviewSetting(@Param("userId") long userId);
}

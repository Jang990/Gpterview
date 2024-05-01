package com.mock.interview.user.infrastructure;

import com.mock.interview.user.domain.model.UsersTechLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersTechLinkRepository extends JpaRepository<UsersTechLink, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM UsersTechLink utl WHERE utl.users.id = :userId and utl.technicalSubjects.id IN :techIds")
    int deleteUserTech(@Param("userId") long userId, @Param("techIds") List<Long> techIds);
}

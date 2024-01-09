package com.mock.interview.interview.infrastructure;

import com.mock.interview.interview.domain.InterviewConversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewConversationRepository extends JpaRepository<InterviewConversation, Long> {
    @Query("Select ic From InterviewConversation ic Where ic.interview.id = :interviewId and ic.isDeleted = false Order By ic.createdAt DESC")
    Page<InterviewConversation> findConversation(@Param(value = "interviewId") long interviewId, Pageable pageable);
}

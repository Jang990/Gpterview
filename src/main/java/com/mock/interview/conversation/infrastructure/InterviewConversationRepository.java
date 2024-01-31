package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.domain.model.InterviewConversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterviewConversationRepository extends JpaRepository<InterviewConversation, Long> {
    @Query("Select ic From InterviewConversation ic Where ic.interview.id = :interviewId and ic.isDeleted = false Order By ic.createdAt DESC")
    Page<InterviewConversation> findConversation(@Param(value = "interviewId") long interviewId, Pageable pageable);

    @Query("""
            SELECT ic FROM InterviewConversation ic 
            WHERE ic.interview.id = :interviewId 
            ORDER BY ic.createdAt DESC 
            LIMIT 1 
            """)
    Optional<InterviewConversation> findLastConversation(@Param("interviewId") long interviewId);

    @Query("""
            SELECT ic FROM InterviewConversation ic 
            WHERE ic.interview.id = :interviewId AND ic.isDeleted = false 
            """)
    Slice<InterviewConversation> findByInterviewId(@Param("interviewId") long interviewId, Pageable pageable);
}

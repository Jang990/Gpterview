package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterviewConversationPairRepository extends JpaRepository<InterviewConversationPair, Long> {
    @Query("""
            SELECT icp FROM InterviewConversationPair icp 
            WHERE icp.interview.id = :interviewId AND icp.question.id = :questionId
            """)
    Optional<InterviewConversationPair> findConversation(@Param("interviewId") long interviewId, @Param("questionId") long questionId);

    @Query("""
            SELECT icp FROM InterviewConversationPair icp 
            WHERE icp.id = :pairId AND icp.interview.id = :interviewId
            """)
    Optional<InterviewConversationPair> findByIdWithInterviewId(@Param("pairId") long pairId, @Param("interviewId") long interviewId);

    @Query("""
            SELECT icp FROM InterviewConversationPair icp
            JOIN FETCH icp.question
            LEFT JOIN FETCH icp.answer
            WHERE icp.interview.id = :interviewId
            """)
    Slice<InterviewConversationPair> findCurrentConversationHistory(@Param("interviewId") long interviewId, Pageable pageable);
}
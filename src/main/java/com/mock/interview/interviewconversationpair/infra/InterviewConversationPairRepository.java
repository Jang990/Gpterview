package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
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
}

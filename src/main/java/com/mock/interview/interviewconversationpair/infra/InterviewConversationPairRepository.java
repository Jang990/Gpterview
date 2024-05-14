package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewConversationPairRepository extends JpaRepository<InterviewConversationPair, Long> {
    @Query("""
            SELECT icp FROM InterviewConversationPair icp 
            WHERE icp.interview.id = :interviewId AND icp.question.id = :questionId
            """)
    Optional<InterviewConversationPair> findConversation(@Param("interviewId") long interviewId, @Param("questionId") long questionId);

    @Query("""
            SELECT icp FROM InterviewConversationPair icp
            JOIN FETCH icp.interview
            JOIN FETCH icp.interview.users
            WHERE icp.id = :pairId AND icp.interview.id = :interviewId AND icp.interview.users.id = :userId
            """)
    Optional<InterviewConversationPair> findWithInterviewUser(@Param("pairId") long pairId,
                                                              @Param("interviewId") long interviewId,
                                                              @Param("userId") long userId);

    @Query("""
            SELECT icp FROM InterviewConversationPair icp 
            WHERE icp.id = :pairId AND icp.interview.id = :interviewId
            """)
    Optional<InterviewConversationPair> findByIdWithInterviewId(@Param("pairId") long pairId, @Param("interviewId") long interviewId);

    @Query(value = """
            SELECT icp FROM InterviewConversationPair icp
            JOIN FETCH icp.question
            JOIN FETCH icp.answer
            WHERE icp.interview.id = :interviewId AND icp.status = 'COMPLETED'
            ORDER BY icp.createdAt DESC
            """)
    List<InterviewConversationPair> findLastCompletedConversation(@Param("interviewId") long interviewId, Pageable pageable);

    @Query(value = """
            SELECT icp FROM InterviewConversationPair icp
            JOIN FETCH icp.question
            JOIN FETCH icp.answer
            WHERE icp.interview.id = :interviewId AND icp.interview.users.id = :userId AND icp.status = 'COMPLETED'
            ORDER BY icp.createdAt DESC
            """)
    List<InterviewConversationPair> findLastCompletedConversation(@Param("userId") long userId, @Param("interviewId") long interviewId, Pageable pageable);

    @Query("""
            SELECT icp FROM InterviewConversationPair icp
            JOIN FETCH icp.question
            LEFT JOIN FETCH icp.answer
            WHERE icp.interview.id = :interviewId
            """)
    Slice<InterviewConversationPair> findCurrentConversationHistory(@Param("interviewId") long interviewId, Pageable pageable);

    @Query("""
            SELECT icp.question.id
            FROM InterviewConversationPair icp
            WHERE icp.interview.id = :interviewId
            """)
    List<Long> findAppearedQuestionIds(@Param("interviewId") long interviewId);

    @Query("""
            UPDATE InterviewConversationPair icp
            SET icp.question = null
            WHERE icp.question.id = :questionId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void removeQuestion(@Param("questionId") long questionId);

    @Query("""
            UPDATE InterviewConversationPair icp
            SET icp.answer = null
            WHERE icp.answer.id = :answerId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void removeAnswer(@Param("answerId") long answerId);

    @Query("""
            UPDATE InterviewConversationPair icp
            SET icp.interview = null
            WHERE icp.interview.id = :interviewId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void removeInterview(@Param("interviewId") long interviewId); // TODO: 추후 추천 기능을 사용할 때 로그로 사용될 수 있다.

}

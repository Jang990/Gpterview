package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewConversationPairRepository extends JpaRepository<InterviewConversationPair, Long> {
}

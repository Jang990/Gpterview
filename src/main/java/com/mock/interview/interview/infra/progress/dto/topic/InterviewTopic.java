package com.mock.interview.interview.infra.progress.dto.topic;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 현재 면접의 주제를 파악할 수 있는 인터페이스
 */
public interface InterviewTopic<T> {
    Long getId();

    String getContent();
    void connectToQuestion(JpaRepository<T, Long> repository, InterviewQuestion question);
}

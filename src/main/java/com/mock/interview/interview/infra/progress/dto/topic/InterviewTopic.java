package com.mock.interview.interview.infra.progress.dto.topic;

import com.mock.interview.interview.infra.progress.InterviewTopicConnector;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;


/**
 * 현재 면접의 주제를 파악할 수 있는 인터페이스
 *
 * 구현체 추가 시 사이드 이펙트 발생 부분
 * @see InterviewTopicConnector
 * @see InterviewProgress
 */
public interface InterviewTopic<T> {
    Long getId();

    String getContent();
    void connectToQuestion(JpaRepository<T, Long> repository, InterviewQuestion question);
}

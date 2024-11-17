package com.mock.interview.interview.domain.model;

import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Interview 팩토리 메소드 내부에 topics 관련 로직으로 인해
 * Interview 팩토리 메소드가 포함된 로직들을 테스트할 Interview 관련 세팅도 해줘야 하는 어려움이 있다.
 * 만약 팩토리 메소드가 간단해진다면 이 클래스는 제거해도 된다.
 */
@Service
@RequiredArgsConstructor
public class InterviewCreator {
    private final InterviewTitleCreator titleCreator;

    protected Interview create(CandidateInfo candidateInfo, InterviewTopicDto topics, InterviewTimer timer) {
        InterviewTitle interviewTitle = titleCreator
                .createDefault(candidateInfo.getCategory(), candidateInfo.getPosition());
        return Interview.create(interviewTitle, timer, candidateInfo, topics);
    }
}

package com.mock.interview.interview.domain;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 면접에 필요한 기술과 경험 주제는 N:M관계라 중간 테이블이 필요하다.
 * 중간 테이블에는 Interview 객체가 필수이므로 InterviewTopic은 생성자에서 만들게 된다.
 * 그래서 생성자까지 데이터를 운반할 이 클래스를 만들었다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewTopicsDto {
    private InterviewType type;
    private List<TechnicalSubjects> techTopics = new LinkedList<>();
    private List<Experience> experienceTopics = new LinkedList<>();

    protected static InterviewTopicsDto ofType(InterviewType type) {
        InterviewTopicsDto result = new InterviewTopicsDto();
        result.type = type;

        return result;
    }

    protected static InterviewTopicsDto ofTypeWithExperiences(
            InterviewType type,
            List<Experience> experiences) {
        InterviewTopicsDto result = new InterviewTopicsDto();
        result.type = type;
        result.techTopics = Collections.emptyList();
        result.experienceTopics = List.copyOf(experiences);

        return result;
    }

    protected static InterviewTopicsDto ofTypeWithTechs(
            InterviewType type,
            List<TechnicalSubjects> techs) {
        InterviewTopicsDto result = new InterviewTopicsDto();
        result.type = type;
        result.experienceTopics = Collections.emptyList();
        result.techTopics = List.copyOf(techs);

        return result;
    }

    protected static InterviewTopicsDto ofTypeWithAll(
            InterviewType type,
            List<Experience> experiences,
            List<TechnicalSubjects> techs) {
        InterviewTopicsDto result = new InterviewTopicsDto();
        result.type = type;
        result.techTopics = List.copyOf(techs);
        result.experienceTopics = List.copyOf(experiences);

        return result;
    }
}

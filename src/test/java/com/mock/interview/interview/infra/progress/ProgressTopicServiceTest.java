package com.mock.interview.interview.infra.progress;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.interview.infra.cache.dto.InterviewConfig;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interview.infra.progress.ProgressTopicService;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgressTopicServiceTest {

    ProgressTopicService selector = new ProgressTopicService();

    List<TechTopic> topics = List.of(
            new TechTopic(1L, "Something1"),
            new TechTopic(2L, "Something2"),
            new TechTopic(2L, "Something3")
    );

    @Test
    @DisplayName("기술 페이즈 리스트")
    void testTopicList1() {
        InterviewPhase phase = InterviewPhase.TECHNICAL;
        List<TechTopic> techTopics = List.of(mock(TechTopic.class));
        List<ExperienceTopic> experienceTopics = List.of(mock(ExperienceTopic.class));

        List<? extends InterviewTopic<?>> result = selector
                .findTopicList(phase, mockTopicInfo(techTopics, experienceTopics));

        assertThat(result).isSameAs(techTopics);
        assertThat(result).isNotSameAs(experienceTopics);
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("경험 페이즈 리스트")
    void testTopicList2() {
        InterviewPhase phase = InterviewPhase.EXPERIENCE;
        List<TechTopic> techs = List.of(mock(TechTopic.class));
        List<ExperienceTopic> experiences = List.of(mock(ExperienceTopic.class));

        List<? extends InterviewTopic<?>> result = selector
                .findTopicList(phase, mockTopicInfo(techs, experiences));

        assertThat(result).isSameAs(experiences);
        assertThat(result).isNotSameAs(techs);
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("인성 페이즈 리스트")
    void testTopicList3() {
        InterviewPhase phase = InterviewPhase.PERSONAL;
        List<TechTopic> techs = List.of(mock(TechTopic.class));
        List<ExperienceTopic> experiences = List.of(mock(ExperienceTopic.class));

        List<? extends InterviewTopic<?>> result = selector
                .findTopicList(phase, mockTopicInfo(techs, experiences));

        assertThat(result).isNotSameAs(techs);
        assertThat(result).isNotSameAs(experiences);
        assertThat(result.isEmpty()).isTrue();
    }

    private InterviewInfo mockTopicInfo(List<TechTopic> techTopics, List<ExperienceTopic> experienceTopics) {
        return new InterviewInfo(0L,
                new InterviewProfile(
                        mock(CategoryResponse.class),
                        mock(CategoryResponse.class),
                        techTopics, experienceTopics
                ),
                mock(InterviewConfig.class)
        );
    }

    @Test
    @DisplayName("첫 번째 토픽 선택")
    void test1() {
        int testIdx = 0;

        TechTopic selected = selector
                .selectTopic(topics, findTopicProgress(topics, testIdx));

        assertThat(selected).isSameAs(topics.get(testIdx));
    }

    @Test
    @DisplayName("두 번째 토픽 선택")
    void test2() {
        int testIdx = 1;

        TechTopic selected = selector
                .selectTopic(topics, findTopicProgress(topics, testIdx));

        assertThat(selected).isSameAs(topics.get(testIdx));
    }

    @Test
    @DisplayName("세 번째 토픽 선택")
    void test3() {
        int testIdx = 2;

        TechTopic selected = selector
                .selectTopic(topics, findTopicProgress(topics, testIdx));

        assertThat(selected).isSameAs(topics.get(testIdx));
    }

    private double findTopicProgress(List<?> topics, int idx) {
        if(idx < 0 || topics.size() <= idx)
            throw new IllegalArgumentException("토픽 리스트 범위를 벗어남.");
        return (double)(idx) / topics.size();
    }
}
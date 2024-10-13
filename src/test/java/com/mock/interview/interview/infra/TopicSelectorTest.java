package com.mock.interview.interview.infra;

import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TopicSelectorTest {

    TopicSelector selector = new TopicSelector();

    List<TechTopic> topics = List.of(
            new TechTopic(1L, "Something1"),
            new TechTopic(2L, "Something2"),
            new TechTopic(2L, "Something3")
    );

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
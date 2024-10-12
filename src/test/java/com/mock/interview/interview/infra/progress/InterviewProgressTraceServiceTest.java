package com.mock.interview.interview.infra.progress;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.interview.infra.cache.dto.InterviewConfig;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewProgressTraceServiceTest {
    @Mock
    ProgressTracker tracker;

    @InjectMocks
    InterviewProgressTraceService traceService;

    List<TechTopic> techTopics = List.of(
            new TechTopic(1L, "Something1"),
            new TechTopic(2L, "Something2"),
            new TechTopic(2L, "Something3")
    );

    List<ExperienceTopic> experienceTopics = List.of(
            new ExperienceTopic(1L, "Something1"),
            new ExperienceTopic(2L, "Something3")
    );

    @Test
    @DisplayName("첫 기술 토픽 테스트")
    void test1() {
        InterviewPhase phase = InterviewPhase.TECHNICAL;
        InterviewInfo info = techInfo(techTopics);
        when(tracker.tracePhase(any(), any())).thenReturn(phase);
        when(tracker.traceProgress(any(), any())).thenReturn(firstTopicProgress());

        InterviewProgress progress = traceService.trace(info);

        assertThat(progress.interviewTopic()).isSameAs(firstTopic(techTopics));
        assertThat(progress.phase()).isEqualTo(phase);
        assertThat(progress.progress()).isEqualTo(firstTopicProgress());
    }

    @Test
    @DisplayName("중간 기술 토픽 테스트")
    void test2() {
        InterviewPhase phase = InterviewPhase.TECHNICAL;
        InterviewInfo info = techInfo(techTopics);
        when(tracker.tracePhase(any(), any())).thenReturn(phase);
        when(tracker.traceProgress(any(), any())).thenReturn(middleTopicProgress(techTopics));

        InterviewProgress progress = traceService.trace(info);

        assertThat(progress.interviewTopic()).isSameAs(middleTopic(techTopics));
        assertThat(progress.phase()).isEqualTo(phase);
        assertThat(progress.progress()).isEqualTo(middleTopicProgress(techTopics));
    }

    @Test
    @DisplayName("마지막 기술 토픽 테스트")
    void test3() {
        InterviewPhase phase = InterviewPhase.TECHNICAL;
        InterviewInfo info = techInfo(techTopics);
        when(tracker.tracePhase(any(), any())).thenReturn(phase);
        when(tracker.traceProgress(any(), any())).thenReturn(lastTopicProgress(techTopics));

        InterviewProgress progress = traceService.trace(info);

        assertThat(progress.interviewTopic()).isSameAs(lastTopic(techTopics));
        assertThat(progress.phase()).isEqualTo(phase);
        assertThat(progress.progress()).isEqualTo(lastTopicProgress(techTopics));
    }

    @Test
    @DisplayName("첫 경험 토픽 확인")
    void test4() {
        InterviewPhase phase = InterviewPhase.EXPERIENCE;
        InterviewInfo info = experienceInfo(experienceTopics);
        when(tracker.tracePhase(any(), any())).thenReturn(phase);
        when(tracker.traceProgress(any(), any())).thenReturn(firstTopicProgress());

        InterviewProgress progress = traceService.trace(info);

        assertThat(progress.phase()).isEqualTo(phase);
        assertThat(progress.progress()).isEqualTo(firstTopicProgress());
        assertThat(progress.interviewTopic()).isSameAs(firstTopic(experienceTopics));
    }

    @Test
    @DisplayName("마지막 경험 토픽 확인")
    void test5() {
        InterviewPhase phase = InterviewPhase.EXPERIENCE;
        InterviewInfo info = experienceInfo(experienceTopics);
        when(tracker.tracePhase(any(), any())).thenReturn(phase);
        when(tracker.traceProgress(any(), any())).thenReturn(lastTopicProgress(experienceTopics));

        InterviewProgress progress = traceService.trace(info);

        assertThat(progress.phase()).isEqualTo(phase);
        assertThat(progress.progress()).isEqualTo(lastTopicProgress(experienceTopics));
        assertThat(progress.interviewTopic()).isSameAs(lastTopic(experienceTopics));
    }

    @Test
    @DisplayName("인성 토픽은 없음")
    void test6() {
        InterviewPhase phase = InterviewPhase.PERSONAL;
        InterviewInfo info = emptyInfo();
        when(tracker.tracePhase(any(), any())).thenReturn(phase);
        when(tracker.traceProgress(any(), any())).thenReturn(firstTopicProgress());

        InterviewProgress progress = traceService.trace(info);

        assertThat(progress.phase()).isEqualTo(phase);
        assertThat(progress.progress()).isEqualTo(firstTopicProgress());
        assertThat(progress.interviewTopic()).isNull();
    }

    private double firstTopicProgress() {
        return 0;
    }

    private double middleTopicProgress(List<?> topics) {
        return (double) 1 / topics.size();
    }

    private double lastTopicProgress(List<?> topics) {
        return (double)(topics.size() - 1) / topics.size();
    }

    private InterviewTopic firstTopic(List<? extends InterviewTopic> topics) {
        return topics.get(0);
    }
    private InterviewTopic middleTopic(List<? extends InterviewTopic> topics) {
        return topics.get(1);
    }

    private InterviewTopic lastTopic(List<? extends InterviewTopic> topics) {
        return topics.get(topics.size() - 1);
    }


    private InterviewInfo techInfo(List<TechTopic> topics) {
        return createInfo(topics, List.of());
    }

    private InterviewInfo experienceInfo(List<ExperienceTopic> topics) {
        return createInfo(List.of(), topics);
    }

    private InterviewInfo emptyInfo() {
        return createInfo(List.of(), List.of());
    }

    private InterviewInfo createInfo(List<TechTopic> techs, List<ExperienceTopic> experiences) {
        return new InterviewInfo(mockId(), new InterviewProfile(mockCategory(), mockCategory(), techs, experiences), mockConfig());
    }

    private CategoryResponse mockCategory() {
        return mock(CategoryResponse.class);
    }

    private long mockId() {
        return 1L;
    }

    private InterviewConfig mockConfig() {
        return mock(InterviewConfig.class);
    }

}
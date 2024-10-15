package com.mock.interview.interview.infra.progress;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewProgressTraceServiceTest {

    @Mock
    ProgressTracer tracer;
    @Mock ProgressTopicService topicService;
    @InjectMocks InterviewProgressTraceService traceService;

    @Test
    @DisplayName("각 정보를 잘 모아서 반환")
    void test1() {
        ExperienceTopic testTopic = mock(ExperienceTopic.class);
        InterviewPhase testPhase = InterviewPhase.EXPERIENCE;
        double testProgress = 0.0;

        when(tracer.tracePhase(any(),any())).thenReturn(testPhase);
        when(tracer.traceProgress(any(), any())).thenReturn(testProgress);
        when(topicService.selectTopic(any(), anyDouble())).thenReturn(testTopic);

        InterviewProgress progress = traceService.trace(mock(InterviewInfo.class));

        assertThat(progress.interviewTopic()).isSameAs(testTopic);
        assertThat(progress.progress()).isEqualTo(testProgress);
        assertThat(progress.phase()).isEqualTo(testPhase);
    }

}
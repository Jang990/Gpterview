package com.mock.interview.infrastructure.interview.strategy.stage;

import com.mock.interview.infrastructure.interview.dto.Message;
import com.mock.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.presentation.dto.InterviewType;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterviewProgressTrackerTest {
    InterviewProgressTracker tracker = new InterviewProgressTracker();

    @Test
    void testTechnical() {
        Set<InterviewStage> set = test100(InterviewType.TECHNICAL);
        assertThat(set).containsExactlyInAnyOrder(InterviewStage.TECHNICAL, InterviewStage.FINISHED);
    }

    @Test
    void testComposite() {
        Set<InterviewStage> set = test100(InterviewType.COMPOSITE);
        assertThat(set).containsExactlyInAnyOrder(InterviewStage.values());
        fail("문제가 많음");
    }

    private Set<InterviewStage> test100(InterviewType type) {
        final int testSize = 100;
        Set<InterviewStage> set = new HashSet<>();

        InterviewDetailsDto detailsDto = new InterviewDetailsDto();
        detailsDto.setInterviewType(type);
        MessageHistory history = mock(MessageHistory.class);
        List<Message> list = mock(LinkedList.class);
        when(list.size()).thenReturn(2);

        InterviewProgress initProgress = tracker.getCurrentInterviewProgress(detailsDto, history);
        InterviewStage prevStage = initProgress.stage();
        double prevProgress = initProgress.progress();

        for (int historySize = 3; historySize < testSize; historySize++) {
            when(list.size()).thenReturn(historySize);
            when(history.getMessages()).thenReturn(list);
            InterviewProgress nowProgress = tracker.getCurrentInterviewProgress(detailsDto, history);

            if (nowProgress.progress() < prevProgress
                    && nowProgress.stage() == prevStage)
                fail("HistorySize가 커졌지만 같은 Stage에서 Percent가 낮아짐");

            set.add(nowProgress.stage());
            if(nowProgress.stage() == InterviewStage.FINISHED)
                break;
        }

        return set;
    }

}
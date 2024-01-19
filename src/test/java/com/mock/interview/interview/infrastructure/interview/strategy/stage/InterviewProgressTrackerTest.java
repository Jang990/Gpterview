package com.mock.interview.interview.infrastructure.interview.strategy.stage;

import com.mock.interview.conversation.infrastructure.interview.dto.InterviewConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewProgress;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewProgressTracker;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewStage;
import com.mock.interview.candidate.presentation.dto.InterviewType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterviewProgressTrackerTest {
    InterviewProgressTracker tracker = new InterviewProgressTracker();

    // MAX_CONVERSATION_COUNT가 3보다 크고 2,3의 공배수라면 잘 동작함 - 예외에 대한 문제가 있김 함
    @Test
    void testTechnical() {
        int[] answerCnt = new int[1];
        Set<InterviewStage> set = test100(InterviewType.TECHNICAL, answerCnt);
        assertThat(set).containsExactlyInAnyOrder(InterviewStage.TECHNICAL, InterviewStage.FINISHED);
    }

    @Test
    void testTechEx() {
        int[] answerCnt = new int[2];
        Set<InterviewStage> set = test100(InterviewType.TECHNICAL_EXPERIENCE, answerCnt);
        assertThat(set).containsExactlyInAnyOrder(InterviewStage.TECHNICAL, InterviewStage.EXPERIENCE, InterviewStage.FINISHED);

        // 각 스테이지별로 골고루 질문을 하는지
        assertThat(answerCnt[0]).isEqualTo(answerCnt[1]);
    }

    @Test
    void testComposite() {
        int[] answerCnt = new int[3];
        Set<InterviewStage> set = test100(InterviewType.COMPOSITE, answerCnt);
        assertThat(set).containsExactlyInAnyOrder(InterviewStage.values());

        // 각 스테이지별로 골고루 질문을 하는지
        assertThat(answerCnt[0]).isEqualTo(answerCnt[1]);
        assertThat(answerCnt[0]).isEqualTo(answerCnt[2]);
    }

    private Set<InterviewStage> test100(InterviewType type, int[] answerCnt) {
        final int testSize = 100;
        int answerIdx = 0;
        Set<InterviewStage> set = new HashSet<>();

        InterviewConfig config = new InterviewConfig(type, LocalDateTime.now());
        MessageHistory history = mock(MessageHistory.class);
        List<Message> list = mock(LinkedList.class);
        when(list.size()).thenReturn(2);
        when(history.getMessages()).thenReturn(list);

        InterviewProgress startProgress = tracker.getCurrentInterviewProgress(config, history);
        InterviewStage prevStage = startProgress.stage();
        double prevProgress = startProgress.progress();
        int cnt = 0;

        for (int historySize = 3; historySize < testSize; historySize++) {
            when(list.size()).thenReturn(historySize);
            InterviewProgress nowProgress = tracker.getCurrentInterviewProgress(config, history);

            if (nowProgress.progress() < prevProgress
                    && nowProgress.stage() == prevStage)
                fail("HistorySize가 커졌지만 같은 Stage에서 Percent가 낮아짐");

//            if (historySize % 2 == 1) {
//                cnt++;
//                System.out.println(cnt + "번째 면접관 질문 - " + historySize + " : " + nowProgress);
//            } else {
//                System.out.println("\t질문에 대한 답변 - " + historySize + " : " + nowProgress);
//            }

            set.add(nowProgress.stage());
            if(nowProgress.stage() == InterviewStage.FINISHED)
                break;

            if (nowProgress.stage() != prevStage) {
                answerIdx++;
            }
            if(historySize % 2 == 1) // 면접관 질문 턴이라면
                answerCnt[answerIdx]++;
            prevStage = nowProgress.stage();
        }

        return set;
    }

}
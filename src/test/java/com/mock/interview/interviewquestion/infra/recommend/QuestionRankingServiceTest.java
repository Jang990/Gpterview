package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.calculator.CosineSimilarityCalculator;
import com.mock.interview.interviewquestion.infra.recommend.calculator.RecommendScorer;
import com.mock.interview.interviewquestion.infra.recommend.calculator.TFIDFCalculator;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionRankingServiceTest {
    @Mock RecommendScorer recommendScorer;
    @Mock TFIDFCalculator tfidfCalculator;
    @Mock CosineSimilarityCalculator cosineSimilarityCalculator;
    @InjectMocks
    QuestionRankingService rankingService;

    @Test
    @DisplayName("사이즈 검증")
    void test() {
        int testSize = 3;
        CurrentConversation testUP = mock(CurrentConversation.class);
        List<QuestionMetaData> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            QuestionMetaData metaData = mock(QuestionMetaData.class);
            list.add(metaData);
        }

        List<Long> top3Ids = null;
        try { top3Ids = rankingService.recommendTechQuestion(testSize, testUP, list); }
        catch (NotEnoughQuestion e) { fail(); }

        assertThat(top3Ids.size()).isEqualTo(testSize);
    }

}
package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.Top3Question;

import java.util.List;

public interface QuestionRecommender {
    List<Long> recommend(int recommendationSize, CurrentConversation currentConversation, List<QuestionMetaData> relatedQuestions) throws NotEnoughQuestion;

    Top3Question recommendTop3(CurrentConversation currentConversation, List<QuestionMetaData> relatedQuestions) throws NotEnoughQuestion;

    Top3Question retryRecommendation(CurrentConversation currentConversation, List<QuestionMetaData> relatedQuestions) throws NotEnoughQuestion;
}

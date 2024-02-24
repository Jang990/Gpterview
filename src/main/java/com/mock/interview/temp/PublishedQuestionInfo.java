package com.mock.interview.temp;

import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewProgress;

public record PublishedQuestionInfo(String createdBy, String question, InterviewProgress progress) {
}

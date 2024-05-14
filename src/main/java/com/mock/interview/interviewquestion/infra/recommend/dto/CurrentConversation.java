package com.mock.interview.interviewquestion.infra.recommend.dto;


import java.util.List;

public record CurrentConversation(Long beforeQuestionId, List<String> beforeAnswerContent, String tech, String field) {
}

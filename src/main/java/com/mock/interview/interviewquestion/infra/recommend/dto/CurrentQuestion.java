package com.mock.interview.interviewquestion.infra.recommend.dto;


import java.util.List;

public record CurrentQuestion(Long beforeQuestionId, List<String> beforeQuestionContent, String tech, String field) {
}

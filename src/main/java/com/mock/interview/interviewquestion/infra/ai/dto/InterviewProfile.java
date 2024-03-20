package com.mock.interview.interviewquestion.infra.ai.dto;

import java.util.List;

public record InterviewProfile(String department, String field, List<String> skills, List<String> experience) {

}

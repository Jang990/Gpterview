package com.mock.interview.interviewquestion.infra.cache.dto;

import java.util.List;

public record InterviewProfile(String category, String field, List<String> skills, List<String> experience) {

}

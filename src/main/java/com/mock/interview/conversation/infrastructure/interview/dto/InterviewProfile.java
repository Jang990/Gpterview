package com.mock.interview.conversation.infrastructure.interview.dto;

import java.util.List;

public record InterviewProfile(String department, String field, List<String> skills, List<String> experience) {

}

package com.mock.interview.interviewquestion.infra.ai.gpt.dto.openai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OpenAIMessage {
    private String role;
    private String content;

    public OpenAIMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}

package com.mock.interview.infrastructure.gpt.dto.openai;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Tool> tool_calls;

    public OpenAIMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    @JsonIgnore
    public String getResponseMessage() {
        return tool_calls.get(0).getFunction().getArguments();
    }

    /**
     * "tool_calls": [
     *           {
     *             "function": {
     *               "name": "getScore",
     *               "arguments": "{\n  \"answer\": \"2\",\n  \"score\": 100\n}"
     *             }
     *           }
     * ]
     */
    @Data
    @NoArgsConstructor
    private static class Tool {
        private Function function;

        @Data
        @NoArgsConstructor
        private static class Function {
            String name;
            String arguments;
        }
    }
}

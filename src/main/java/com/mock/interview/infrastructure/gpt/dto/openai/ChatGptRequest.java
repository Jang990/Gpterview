package com.mock.interview.infrastructure.gpt.dto.openai;

import com.mock.interview.presentaion.web.dto.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatGptRequest {
    private String model;
    private List<OpenAIMessage> messages;
    private final int n = 1;
    private final double temperature = 0.7;

    public ChatGptRequest(String model, List<OpenAIMessage> history) {
        this.model = model;
        this.messages = history;
    }
}

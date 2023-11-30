package com.mock.interview.gpt.dto;

import com.mock.interview.contorller.dto.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatGptRequest {
    private String model;
    private List<Message> messages;
    private final int n = 1;
    private double temperature;

    public ChatGptRequest(String model, List<Message> history) {
        this.model = model;
        this.messages = history;
    }
}

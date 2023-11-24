package com.mock.interview.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatResponse {

    private List<Choice> choices;

    // constructors, getters and setters

    @Data
    @AllArgsConstructor
    public static class Choice {

        private int index;
        private Message message;

        // constructors, getters and setters
    }
}
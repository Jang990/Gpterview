package com.mock.interview.presentaion.web.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MessageHistoryDto {
    private List<MessageDto> messages;

    public MessageHistoryDto() {
        this.messages = new LinkedList<>();
    }
}

package com.mock.interview.presentaion.web.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MessageHistoryDTO {
    private List<MessageDTO> messages;

    public MessageHistoryDTO() {
        this.messages = new LinkedList<>();
    }
}

package com.mock.interview.presentation.dto;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MessageHistoryDto {
    @Valid
    private List<MessageDto> messages;

    public MessageHistoryDto() {
        this.messages = new LinkedList<>();
    }
}

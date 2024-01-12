package com.mock.interview.interview.infrastructure.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.interview.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.presentation.dto.MessageHistoryDto;

public class AIObjectConvertor {
    private static final ObjectMapper om = new ObjectMapper();

    public static MessageHistory dtoToObject(MessageHistoryDto historyDTO) {
        try {
            String json = om.writeValueAsString(historyDTO);
            return om.readValue(json, MessageHistory.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // TODO: 커스텀 예외 추가
        }
    }
}

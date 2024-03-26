package com.mock.interview.interview.presentation.dto.message;

import java.util.List;

public record PublishedQuestionDto(Long conversationPairId, List<MessageDto> question){
}

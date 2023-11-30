package com.mock.interview.gpt.dto;

import com.mock.interview.contorller.dto.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatGptResponse {

    private List<Choice> choices;
    private Usage usage;

    @Data
    @NoArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
    }

    @Data
    @NoArgsConstructor
    public static class Usage {
        private int prompt_tokens; // 프롬프트의 토큰 수입니다.
        private int completion_tokens; // 생성된 완료의 토큰 수입니다.
        private int total_tokens; // (프롬프트 + 완료)
    }
}
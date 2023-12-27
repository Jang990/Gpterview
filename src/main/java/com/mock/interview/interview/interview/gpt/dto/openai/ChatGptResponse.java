package com.mock.interview.interview.interview.gpt.dto.openai;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        private OpenAIMessage message;
    }

    @JsonIgnore
    public String getFunctionResultString() {
        return choices.get(0).message.getResponseMessage();
    }

    @Data
    @NoArgsConstructor
    public static class Usage {
        private int prompt_tokens; // 프롬프트의 토큰 수입니다.
        private int completion_tokens; // 생성된 완료의 토큰 수입니다.
        private int total_tokens; // (프롬프트 + 완료)
    }
}
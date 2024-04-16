package com.mock.interview.interviewquestion.infra.ai.gpt.dto.openai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatGptResponse {

    private List<Choice> choices; // GPT 응답 결과 객체
    private Usage usage; // 현재 요청의 토큰 사용량

    @JsonIgnore
    public String getResult() {
        if (isEmptyChoice()) {
            throw new IllegalArgumentException("ChatGPT Message Response 데이터 누락 오류"); // TODO: 커스텀 예외로 바꿀 것
        }
        return choices.get(0).message.getContent();
    }

    @JsonIgnore
    private boolean isEmptyChoice() {
        return choices == null || choices.isEmpty();
    }

    @Data
    @NoArgsConstructor
    public static class Choice {
        private int index;
        private OpenAIMessage message;
    }

    @Data
    @NoArgsConstructor
    public static class Usage {
        private int prompt_tokens; // 프롬프트의 토큰 수입니다.
        private int completion_tokens; // 생성된 완료의 토큰 수입니다.
        private int total_tokens; // (프롬프트 + 완료)
    }
}
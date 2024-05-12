package com.mock.interview.interviewquestion.infra.gpt.prompt;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AiPrompt {
    private final String prompt; // 면접관으로 면접을 진행해라. ~기술을 주제로 질문해라.
    private final String additionalUserInfo; // 너무 긴 추가 정보 - ex) 나는 ~을 했고 ~을 개선했고 ... 뭘 하고 또 뭘하고...
    protected AiPrompt(String prompt) {
        this.prompt = prompt;
        this.additionalUserInfo = null;
    }

    protected AiPrompt(String prompt, String additionalUserInfo) {
        this.prompt = prompt;
        this.additionalUserInfo = additionalUserInfo;
    }

    public boolean hasAdditionalUserInfo() {
        return additionalUserInfo != null && !additionalUserInfo.isBlank();
    }
}
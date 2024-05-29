package com.mock.interview.interviewquestion.infra.gpt.dto.openai;

import com.mock.interview.interviewquestion.infra.gpt.ChatGPTRequester;
import com.mock.interview.interviewquestion.infra.gpt.prompt.AiPrompt;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 다음 구조를 정의
 * {
 *   "model": "gpt-3.5-turbo",
 *   "temperature": 0.7,
 *   "messages": [
 *         {"role": "assistant", "content": "안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?"},
 *         {"role": "user", "content": "네. 준비됐습니다."},
 *         {"role": "system", "content": "너는 IT 분야 백엔드 포지션의 면접관.
 *              메시지가 길다고 마음대로 면접을 마무리하지마. 항상 단 하나의 질문만. 이미 대화한 주제를 질문하지마.
 *              항상 3문장 이하로 응답. 항상 마지막 문장은 질문으로 끝내. 지원자의 응답에 대한 평가는 간단.
 *              질문은 자세히. Spring 기술을 주제로 질문.  지원자의 수준을 파악해야해.
 *              간단한 개념질문보다 컴퓨터 전공자 수준의 응용질문을 해.기술면접을 진행해."
 *          },
 *         ...
 *     ]
 * }
 */
@Data
@NoArgsConstructor
public class ChatGptRequest {
    private String model;
    private List<OpenAIMessage> messages;
    private final int n = 1;
    private final double temperature = 0.5;

    public static ChatGptRequest create(String model, List<OpenAIMessage> history, AiPrompt orderPrompt) {
        ChatGptRequest request = new ChatGptRequest();
        request.model = model;
        request.messages = history;
        if(orderPrompt.hasAdditionalUserInfo())
            request.messages.add(new OpenAIMessage(ChatGPTRequester.SYSTEM_ROLE, orderPrompt.getAdditionalUserInfo()));
        request.messages.add(new OpenAIMessage(ChatGPTRequester.SYSTEM_ROLE, orderPrompt.getPrompt()));
        return request;
    }
}

package com.mock.interview.interview.interview.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.interview.interview.interview.gpt.dto.openai.ChatGptResponse;
import com.mock.interview.interview.interview.gpt.dto.openai.GptFunctionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class OpenAIResponseConvertor {
    private ObjectMapper om = new ObjectMapper();

    /**
     * Json 형식의 String을 GptFunctionResult 객체로 변환
     * @param response OpenAI 서버의 ChatGpt Request로 받은 Response
     * @return
     */
    public GptFunctionResult convertFunctionResult(ChatGptResponse response) {
        validateResponse(response);

        String messageString = response.getFunctionResultString();
        if(!messageString.contains("\"response\":"))
            return new GptFunctionResult(messageString); // {} 처리 부족

        // TODO : {} or 빈문자로 응답하는 문제 해결하기. - Test 코드 확인할 것
        try {
            JsonNode jsonNode = om.readTree(messageString);
            return new GptFunctionResult(jsonNode.get("response").asText("재시도 해주세요"));
        } catch (JsonProcessingException e) {
            // response 내에 \"가 있다면 예외 발생 가능.
            Pattern pattern = Pattern.compile("\"response\"\\s*:\\s*\"([^\"]*)\"");
            Matcher matcher = pattern.matcher(messageString);
            if(matcher.find())
                return new GptFunctionResult(matcher.group(1));

            log.error("원본-{} : ObjectMapper 변환 오류", messageString, e);
            throw new RuntimeException("ObjectMapper 변환 오류", e); // TODO: 커스텀 예외로 바꿀 것
        }
    }

    /**
     * 서버로 받은 Response 검증.
     * response가 null이거나, choices가 null이거나 비어있을 시.
     * GPT를 이용하여 Rule에 맞는 함수 호출로 만든 결과물이 없을 시
     * 예외를 던짐
     */
    private void validateResponse(ChatGptResponse response) {
        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()
                || !StringUtils.hasText(response.getFunctionResultString())
        ) {
            throw new RuntimeException("ChatGPT Response 데이터 누락 오류"); // TODO: 커스텀 예외로 바꿀 것
        }
    }
}

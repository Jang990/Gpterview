package com.mock.interview.infrastructure.interview.gpt.dto.openai;

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
 *         {"role": "assistant", "content": "운영체제의 프로세스와 스레드의 차이점에 대해 설명해주세요."},
 *         ...
 *     ],
 *   "tools": [
 *     {
 *       "type": "function",
 *       "function": {
 *         "name": "setRule",
 *         "description": " 너는 {IT} 분야 {백엔드} 포지션의 면접관. 항상 단 하나의 질문만 해.",
 *         "parameters": {
 *           "type": "object",
 *           "properties": {
 *             "response": {"type": "string"}
 *           }
 *         }
 *       }
 *     }
 *   ],
 *   "tool_choice": {"type": "function", "function": {"name": "setRule"}}
 * }
 */
@Data
@NoArgsConstructor
public class ChatGptRequest {
    private String model;
    private List<OpenAIMessage> messages;
    private List<Tool> tools;
    private ToolChoice tool_choice;
    private final int n = 1;
    private final double temperature = 0.7;

    public static ChatGptRequest createStartingRequest(String model, List<OpenAIMessage> history) {
        ChatGptRequest request = new ChatGptRequest();
        request.model = model;
        request.messages = history;
        return request;
    }

    public static ChatGptRequest createRequestWithFunction(String model, String functionLogic, List<OpenAIMessage> history) {
        final String functionName = "responseInterview";
        ChatGptRequest request = new ChatGptRequest();
        request.model = model;
        request.messages = history;
        request.tool_choice = new ToolChoice(functionName);
        request.tools = List.of(new Tool(functionName, functionLogic));
        return request;
    }

    /**
     * "function": {"name": "setRule"}
     */
    @Data @NoArgsConstructor
    private static class Function {
        private String name;
        private String description;
        private Parameters parameters;
        private String[] required = {"response"};

        public Function(String functionName) {
            this.name = functionName;
        }

        public Function(String functionName, String rule) {
            this.name = functionName;
            this.description = rule;
            parameters = new Parameters();
        }

        /**
         * "parameters": { "type": "object", "properties": {...} }
         */
        @Data @NoArgsConstructor
        private static class Parameters {
            private final String type = "object";
            private Properties properties = new Properties();

            @Data @NoArgsConstructor
            private static class Properties {
                private Response response = new Response();
            }

            @Data @NoArgsConstructor
            private static class Response {
                private final String type = "string";
            }
        }
    }

    @Data @NoArgsConstructor
    private static class Tool {
        private final String type = "function";
        private Function function;

        public Tool(String functionName, String rule) {
            this.function = new Function(functionName, rule);
        }
    }


    /**
     * {"type": "function", "function": {"name": "setRule"}}
     */
    @Data
    public static class ToolChoice {
        private final String type = "function";
        private final Function function;

        public ToolChoice(String functionName) {
            this.function = new Function(functionName);
        }
    }
}

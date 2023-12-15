package com.mock.interview.infrastructure.gpt.dto.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptFunctionResult {
    private String response;
}

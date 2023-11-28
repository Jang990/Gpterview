package com.mock.interview.contorller;

import com.mock.interview.gpt.ChatRequest;
import com.mock.interview.gpt.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class ChatController {
    
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${openai.model}")
    private String model;
    
    @Value("${openai.api.url}")
    private String apiUrl;
    
    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        ChatRequest request = new ChatRequest(model, message);
        log.info("요청 : {}", request);

        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        log.info("응답 : {}", response);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        
        return response.getChoices().get(0).getMessage().getContent();
    }
}
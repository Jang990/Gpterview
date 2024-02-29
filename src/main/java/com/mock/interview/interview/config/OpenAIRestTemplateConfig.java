package com.mock.interview.interview.config;

import com.mock.interview.interviewquestion.infra.interview.gpt.AISpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class OpenAIRestTemplateConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Bean
    @Qualifier("openaiRestTemplate")
    public RestTemplate openaiRestTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder
                .setConnectTimeout(Duration.ofMillis(AISpecification.CONNECT_TIMEOUT_MS))
                .setReadTimeout(Duration.ofMillis(AISpecification.READ_TIMEOUT_MS))
                .build();

        restTemplate.getInterceptors().add(
                (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
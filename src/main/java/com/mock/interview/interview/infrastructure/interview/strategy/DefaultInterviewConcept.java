package com.mock.interview.interview.infrastructure.interview.strategy;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DefaultInterviewConcept {
    @Value("${interview.strategy.default.personal}")
    private String personal;
    @Value("${interview.strategy.default.experience}")
    private String experience;
    @Value("${interview.strategy.default.technical}")
    private String technical;

    @Value("${interview.strategy.common.skip}")
    private String changingTopicCommand;
}

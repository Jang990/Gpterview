package com.mock.interview.interview.infrastructure.interview.strategy;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ITInterviewConcept {
    @Value("${interview.strategy.IT.personal}")
    private String personal;
    @Value("${interview.strategy.IT.experience}")
    private String experience;
    @Value("${interview.strategy.IT.technical}")
    private String technical;

    @Value("${interview.strategy.common.skip}")
    private String changingTopicCommand;
}

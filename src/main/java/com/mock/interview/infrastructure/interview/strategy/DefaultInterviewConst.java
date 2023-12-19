package com.mock.interview.infrastructure.interview.strategy;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DefaultInterviewConst {
    @Value("${interview.strategy.common.skip}")
    private String changingTopicCommand;
}

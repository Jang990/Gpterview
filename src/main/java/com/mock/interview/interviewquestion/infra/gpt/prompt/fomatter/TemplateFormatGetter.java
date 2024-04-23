package com.mock.interview.interviewquestion.infra.gpt.prompt.fomatter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TemplateFormatGetter {
    @Value("${interview.format.profile.field}")
    private String fieldFormat;

    @Value("${interview.format.profile.category}")
    private String categoryFormat;

    @Value("${interview.format.profile.topic}")
    private String topicFormat;
}

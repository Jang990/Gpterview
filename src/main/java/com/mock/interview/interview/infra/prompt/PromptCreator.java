package com.mock.interview.interview.infra.prompt;

import com.mock.interview.interview.infra.prompt.configurator.PromptConfiguration;
import com.mock.interview.interview.infra.prompt.fomatter.TemplateConstGetter;
import com.mock.interview.interview.infra.prompt.fomatter.StringFormatter;
import com.mock.interview.interviewquestion.infra.gpt.AISpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * InterviewSetting을 생성하는 로직을 담은 생성기 클래스
 */
@Component
@RequiredArgsConstructor
public class PromptCreator {
    private final TemplateConstGetter templateConstGetter;

    /**
     * @param aiSpec  getUserRole()은 "user", getInterviewerRole()은 "assistant" 일 때... (다른 필드들도 변환해줌)
     * @param promptConfiguration 프롬프트로 변환할 정보들
     * @return user는 지원자. assistant는 면접관입니다. user의 기술은 Java, MySQL, Spring입니다.
     */
    public AiPrompt create(AISpecification aiSpec, PromptConfiguration promptConfiguration) {
        Map<String, String> parameters = this.getFormatParameter(aiSpec, promptConfiguration);
        return new AiPrompt(StringFormatter.format(promptConfiguration.getPromptTemplate(), parameters));
    }

    public AiPrompt changeTopic(AISpecification aiSpec, PromptConfiguration promptConfiguration) {
        Map<String, String> parameters = this.getFormatParameter(aiSpec, promptConfiguration);
        String changeTopicPromptTemplate = promptConfiguration.getPromptTemplate().concat(templateConstGetter.getChangingTopicCommand());
        return new AiPrompt(StringFormatter.format(changeTopicPromptTemplate, parameters));
    }



    /**
     * 변환할 키와 값을 담은 Map
     * null 허용
     *
     * @return {
     *      // openAI 기준 예시
     *     {"system":"system"},
     *     {"user":"user"},
     *     {"interviewer":"assistant"},
     *     {"category":"IT"},
     *     {"field":"백엔드"},
     *     {"topic":"Java, MySQL, Spring" or "저는 ~프로젝트를 진행하며 ... ~문제를 해결. ~기술 사용 ..."}
     * }
     */
    private Map<String, String> getFormatParameter(AISpecification aiSpec, PromptConfiguration creationInfo) {
        Map<String, String> map = new HashMap<>();
        map.put(templateConstGetter.getSystemRole(), aiSpec.getSystemRole());
        map.put(templateConstGetter.getUserRole(), aiSpec.getUserRole());
        map.put(templateConstGetter.getInterviewerRole(), aiSpec.getInterviewerRole());
        map.put(templateConstGetter.getCategory(), creationInfo.getCategory());
        map.put(templateConstGetter.getField(), creationInfo.getField());
        map.put(templateConstGetter.getTopic(), creationInfo.getTopic());
        return map;
//        return Map.of(
//                formatConstGetter.getSystemRole(), aiSpec.getSystemRole(),
//                formatConstGetter.getUserRole(), aiSpec.getUserRole(),
//                formatConstGetter.getInterviewerRole(), aiSpec.getInterviewerRole(),
//                formatConstGetter.getField(), creationInfo.getField(),
//                formatConstGetter.getcategory(), creationInfo.getcategory(),
//                formatConstGetter.getSkillsFormat(), creationInfo.getSkills(),
//                formatConstGetter.getExperience(), creationInfo.getExperience()
//        );
    }

}

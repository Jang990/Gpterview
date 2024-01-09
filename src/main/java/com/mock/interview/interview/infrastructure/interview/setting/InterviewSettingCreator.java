package com.mock.interview.interview.infrastructure.interview.setting;

import com.mock.interview.interview.infrastructure.interview.dto.PromptCreationInfo;
import com.mock.interview.interview.infrastructure.interview.fomatter.FormatConstGetter;
import com.mock.interview.interview.infrastructure.interview.fomatter.StringFormatter;
import com.mock.interview.interview.infrastructure.interview.gpt.AISpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * InterviewSetting을 생성하는 로직을 담은 생성기 클래스
 */
@Component
@RequiredArgsConstructor
public class InterviewSettingCreator {
    private final FormatConstGetter formatConstGetter;

    /**
     * @param aiSpec  getUserRole()은 "user", getInterviewerRole()은 "assistant" 일 때... (다른 필드들도 변환해줌)
     * @param creationInfo 프롬프트로 변환할 정보들
     * @return user는 지원자. assistant는 면접관입니다. user의 기술은 Java, MySQL, Spring입니다.
     */
    public InterviewSetting create(AISpecification aiSpec, PromptCreationInfo creationInfo) {
        Map<String, Object> parameters = this.getFormatParameter(aiSpec, creationInfo);
        return new InterviewSetting(StringFormatter.format(creationInfo.promptTemplate(), parameters));
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
     *     {"department":"IT"},
     *     {"field":"백엔드"},
     *     {"skills":"Java, MySQL, Spring"},
     *     {"experience":"저는 ~프로젝트를 진행하며 ... ~문제를 해결. ~기술 사용 ..."},
     * }
     */
    private Map<String, Object> getFormatParameter(AISpecification aiSpec, PromptCreationInfo creationInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put(formatConstGetter.getSystemRole(), aiSpec.getSystemRole());
        map.put(formatConstGetter.getUserRole(), aiSpec.getUserRole());
        map.put(formatConstGetter.getInterviewerRole(), aiSpec.getInterviewerRole());
        map.put(formatConstGetter.getField(), creationInfo.field());
        map.put(formatConstGetter.getDepartment(), creationInfo.department());
        map.put(formatConstGetter.getSkills(), creationInfo.skills());
        map.put(formatConstGetter.getExperience(), creationInfo.experience());
        return map;
//        return Map.of(
//                formatConstGetter.getSystemRole(), aiSpec.getSystemRole(),
//                formatConstGetter.getUserRole(), aiSpec.getUserRole(),
//                formatConstGetter.getInterviewerRole(), aiSpec.getInterviewerRole(),
//                formatConstGetter.getField(), creationInfo.getField(),
//                formatConstGetter.getDepartment(), creationInfo.getDepartment(),
//                formatConstGetter.getSkillsFormat(), creationInfo.getSkills(),
//                formatConstGetter.getExperience(), creationInfo.getExperience()
//        );
    }

}

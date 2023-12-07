package com.mock.interview.infrastructure.interview.setting;

import com.mock.interview.infrastructure.fomatter.FormatConstGetter;
import com.mock.interview.infrastructure.fomatter.StringFormatter;
import com.mock.interview.infrastructure.gpt.AISpecification;
import com.mock.interview.presentaion.web.dto.CandidateProfileDTO;
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
     * @param profile profile.getSkills()는 "Java, MySQL, Spring"일 때 (다른 필드들도 변환해줌)
     * @param concept $_user_는 지원자. $_interviewer_는 면접관입니다. $_user_의 기술은 $_skills_입니다.
     * @return user는 지원자. assistant는 면접관입니다. user의 기술은 Java, MySQL, Spring입니다.
     */
    public InterviewSetting create(AISpecification aiSpec, CandidateProfileDTO profile, String concept) {
        Map<String, Object> parameters = this.getFormatParameter(aiSpec, profile);
        return new InterviewSetting(StringFormatter.format(concept, parameters));
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
    private Map<String, Object> getFormatParameter(AISpecification aiSpec, CandidateProfileDTO profile) {
        Map<String, Object> map = new HashMap<>();
        map.put(formatConstGetter.getSystemRole(), aiSpec.getSystemRole());
        map.put(formatConstGetter.getUserRole(), aiSpec.getUserRole());
        map.put(formatConstGetter.getInterviewerRole(), aiSpec.getInterviewerRole());
        map.put(formatConstGetter.getField(), profile.getField());
        map.put(formatConstGetter.getDepartment(), profile.getDepartment());
        map.put(formatConstGetter.getSkills(), profile.getSkills());
        map.put(formatConstGetter.getExperience(), profile.getExperience());
        return map;
//        return Map.of(
//                formatConstGetter.getSystemRole(), aiSpec.getSystemRole(),
//                formatConstGetter.getUserRole(), aiSpec.getUserRole(),
//                formatConstGetter.getInterviewerRole(), aiSpec.getInterviewerRole(),
//                formatConstGetter.getField(), profile.getField(),
//                formatConstGetter.getDepartment(), profile.getDepartment(),
//                formatConstGetter.getSkillsFormat(), profile.getSkills(),
//                formatConstGetter.getExperience(), profile.getExperience()
//        );
    }

}

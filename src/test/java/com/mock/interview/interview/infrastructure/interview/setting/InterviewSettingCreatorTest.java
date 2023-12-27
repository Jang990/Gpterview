package com.mock.interview.interview.infrastructure.interview.setting;

import com.mock.interview.interview.infrastructure.MockAiSpecCreator;
import com.mock.interview.interview.interview.fomatter.FormatConstGetter;
import com.mock.interview.interview.interview.gpt.AISpecification;
import com.mock.interview.interview.interview.setting.InterviewSettingCreator;
import com.mock.interview.interview.presentation.dto.CandidateProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InterviewSettingCreatorTest {
    InterviewSettingCreator creator;

    @BeforeEach
    void before() {
        creator = new InterviewSettingCreator(createMockFormatConstGetter());
    }

    @Test
    void success() {
        AISpecification spec = MockAiSpecCreator.createMock();
        CandidateProfileDto profile = createProfile();
        String concept = "$_system_ $_user_ $_interviewer_ " +
                "$_department_ $_field_ $_skills_ " +
                "$_experience_";

        String result = creator.create(spec, profile, concept).getConcept();

        // system user null IT 백엔드 Java MySQL Spring 나는 ...를 경험했다.
        assertThat(result).contains(spec.getInterviewerRole());
        assertThat(result).contains(spec.getSystemRole());
        assertThat(result).contains(spec.getUserRole());
        assertThat(result).contains(profile.getDepartment());
        assertThat(result).contains(profile.getField());
        assertThat(result).contains(profile.getSkills());
        assertThat(result).contains(profile.getExperience());
    }

    @Test
    void successWithNull() {
        AISpecification spec = MockAiSpecCreator.createMock();
        CandidateProfileDto profile = createProfile();
        String concept = "$_system_ $_user_ $_interviewer_ " +
                "$_department_ $_WrongKey_ $_field_ $_skills_ ";

        String result = creator.create(spec, profile, concept).getConcept();

        // system user interviewer IT null 백엔드 Java MySQL Spring
        assertThat(result).contains(spec.getInterviewerRole());
        assertThat(result).contains(spec.getSystemRole());
        assertThat(result).contains(spec.getUserRole());
        assertThat(result).contains(profile.getDepartment());
        assertThat(result).contains(profile.getField());
        assertThat(result).contains(profile.getSkills());
        assertThat(result).contains("null");
    }

    private CandidateProfileDto createProfile() {
        CandidateProfileDto profile = new CandidateProfileDto();
        profile.setDepartment("IT");
        profile.setField("백엔드");
        profile.setSkills("Java MySQL Spring");
        profile.setExperience("나는 ...를 경험했다.");
        return profile;
    }

    FormatConstGetter createMockFormatConstGetter() {
        FormatConstGetter getter = Mockito.mock(FormatConstGetter.class);
        Mockito.when(getter.getSystemRole()).thenReturn("system");
        Mockito.when(getter.getUserRole()).thenReturn("user");
        Mockito.when(getter.getInterviewerRole()).thenReturn("interviewer");
        Mockito.when(getter.getField()).thenReturn("field");
        Mockito.when(getter.getDepartment()).thenReturn("department");
        Mockito.when(getter.getSkillsFormat()).thenReturn("skills");
        Mockito.when(getter.getExperience()).thenReturn("experience");
        return getter;
    }
}
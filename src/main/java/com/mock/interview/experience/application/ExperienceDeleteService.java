package com.mock.interview.experience.application;

import com.mock.interview.experience.application.helper.ExperienceVerifyHelper;
import com.mock.interview.experience.infra.ExperienceExistsRepository;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.experience.infra.InterviewExperienceLinkRepository;
import com.mock.interview.interviewanswer.infra.InterviewAnswerDeleteRepository;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairDeleteRepository;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExperienceDeleteService {
    private final ExperienceRepository experienceRepository;
    private final ExperienceExistsRepository experienceExistsRepository;
    private final InterviewExperienceLinkRepository interviewExperienceLinkRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerDeleteRepository answerDeleteRepository;
    private final InterviewConversationPairDeleteRepository conversationPairDeleteRepository;

    /** Experience만 제거 */
    public void delete(long experienceId, long userId) {
        // TODO: 진행중인 면접이 있다면 예외를 발생시킬 것.
        ExperienceVerifyHelper.verify(experienceExistsRepository, experienceId, userId);

        interviewExperienceLinkRepository.deleteByExperienceId(experienceId);
        questionRepository.removeByExperienceId(experienceId);
        experienceRepository.deleteById(experienceId);
    }

    /** Question을 함께 제거하면서 관련 데이터 NULL로 초기화 */
    public void deleteWithQuestion(long experienceId, long userId) {
        ExperienceVerifyHelper.verify(experienceExistsRepository, experienceId, userId);

        interviewExperienceLinkRepository.deleteByExperienceId(experienceId);
        conversationPairDeleteRepository.removeExperienceQuestion(experienceId);
        answerDeleteRepository.removeExperienceQuestion(experienceId);
        questionRepository.deleteByExperienceId(experienceId);
        experienceRepository.deleteById(experienceId);
    }
}

package com.mock.interview.interview.application;

import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.InterviewCreator;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.infra.lock.creation.InterviewCreationUserLock;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.interview.presentation.dto.InterviewStartingDto;
import com.mock.interview.interviewconversationpair.domain.ConversationStarter;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewConfig;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.candidate.domain.exception.CandidateConfigNotFoundException;
import com.mock.interview.candidate.infra.CandidateConfigRepository;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewCreator interviewCreator;
    private final InterviewRepository repository;
    private final InterviewConversationPairRepository pairRepository;
    private final CandidateConfigRepository profileRepository;
    private final ConversationStarter conversationStarter;
    private final InterviewQuestionRepository interviewQuestionRepository;

    @InterviewCreationUserLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public InterviewStartingDto create(long loginId, long candidateConfigId) {
        CandidateConfig candidateConfig = profileRepository.findInterviewConfig(candidateConfigId, loginId)
                .orElseThrow(CandidateConfigNotFoundException::new);

        Users users = candidateConfig.getUsers();
        Interview interview = interviewCreator.startInterview(repository, candidateConfig, users, candidateConfig.getPosition());
        InterviewConversationPair conversationPair = startConversation(candidateConfig, interview);
        return convert(interview, conversationPair);
    }

    private static InterviewStartingDto convert(Interview interview, InterviewConversationPair conversationPair) {
        return new InterviewStartingDto(interview.getId(), new InterviewConversationPairDto(conversationPair.getId(), conversationPair.getStatus()));
    }

    private InterviewConversationPair startConversation(CandidateConfig candidateConfig, Interview interview) {
        long questionCount = interviewQuestionRepository.countCategoryQuestion(candidateConfig.getCategory().getName());
        return conversationStarter.start(pairRepository, interview, questionCount);
    }

    @Transactional(readOnly = true)
    public Optional<InterviewResponse> findActiveInterview(long userId) {
        Optional<Interview> optionalInterview = repository.findActiveInterview(userId);
        if(optionalInterview.isEmpty())
            return Optional.empty();

        return Optional.of(convert(optionalInterview.get()));
    }

    private InterviewResponse convert(Interview activeInterview) {
        return new InterviewResponse(activeInterview.getId(), activeInterview.getTitle().getTitle());
    }

    @Transactional(readOnly = true)
    public InterviewInfo findInterviewForAIRequest(long loginId, long interviewId) {
        Interview interview = repository.findInterviewSetting(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        return convert(interview, interview.getCandidateConfig().getCategory(), interview.getCandidateConfig().getPosition());
    }

    private static InterviewInfo convert(Interview interview, JobCategory category, JobPosition position) {
        CandidateConfig profile = interview.getCandidateConfig();
        return new InterviewInfo(
                new InterviewProfile(
                        category.getName(), position.getName(),
                        profile.getTechSubjects().stream().map(TechnicalSubjects::getName).toList(),
                        profile.getExperienceContent()
                ),
                new InterviewConfig(interview.getCandidateConfig().getType(), interview.getCreatedAt(), interview.getExpiredTime())
        );
    }
}

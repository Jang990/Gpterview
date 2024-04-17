package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infra.lock.progress.InterviewProgressLock;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewLockDto;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.category.infra.CategoryModuleFinder;
import com.mock.interview.interview.domain.InterviewStarter;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.infra.lock.creation.InterviewCreationUserLock;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.interview.presentation.dto.InterviewStartingDto;
import com.mock.interview.interviewconversationpair.application.ConversationConvertor;
import com.mock.interview.interviewconversationpair.domain.ConversationStarter;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.tech.application.TechSavingHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.tech.infra.view.CategoryRelatedTechFinder;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository repository;
    private final InterviewConversationPairRepository pairRepository;
    private final ConversationStarter conversationStarter;
    private final UserRepository userRepository;
    private final List<CategoryRelatedTechFinder> categoryRelatedTechFinders;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final InterviewStarter interviewStarter;


    @InterviewCreationUserLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public InterviewStartingDto createCustomInterview(long loginId, InterviewConfigForm interviewConfig) {
        Users users = userRepository.findForInterviewSetting(loginId)
                .orElseThrow(UserNotFoundException::new);

        Interview interview = interviewStarter.start(users, interviewConfig);
        List<TechnicalSubjects> relatedInterviewTech = getRelatedCategoryTech(users.getCategory());
        interview.linkTech(relatedInterviewTech);
        repository.save(interview);

        InterviewConversationPair conversationPair = conversationStarter.start(pairRepository, interview);
        return convert(interview, conversationPair);
    }

    private List<TechnicalSubjects> getRelatedCategoryTech(JobCategory category) {
        List<String> relatedTechName = CategoryModuleFinder.findModule(categoryRelatedTechFinders, category.getName()).getRelatedTechName();
        return TechSavingHelper.saveTechIfNotExist(technicalSubjectsRepository, relatedTechName);
    }

    private static InterviewStartingDto convert(Interview interview, InterviewConversationPair conversationPair) {
        return new InterviewStartingDto(interview.getId(), ConversationConvertor.convert(conversationPair));
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

    @InterviewProgressLock
    public void expireInterview(InterviewLockDto lockDto) {
        Interview interview = repository.findByIdAndUserId(lockDto.getInterviewId(), lockDto.getUserId())
                .orElseThrow(InterviewNotFoundException::new);
        interview.expire();
    }
}

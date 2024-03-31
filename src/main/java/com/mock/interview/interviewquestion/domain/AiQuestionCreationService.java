package com.mock.interview.interviewquestion.domain;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.application.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interviewquestion.domain.model.TopicChangedQuestionCreatedEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiQuestionCreationService {
    public InterviewQuestion save(
            InterviewQuestionRepository repository, long conversationPairId, Interview interview,
            RecommendedQuestion recommendedQuestion, List<TechnicalSubjects> techList
    ) {
        InterviewQuestion question = createQuestion(repository, interview, recommendedQuestion, techList);
        Events.raise(new ConversationQuestionCreatedEvent(conversationPairId, question.getId()));
        return question;
    }

    public void saveTopicChangedQuestion(
            InterviewQuestionRepository repository,
            Interview interview, InterviewConversationPair conversationPair,
            RecommendedQuestion recommendedQuestion, List<TechnicalSubjects> techList
    ) {
        InterviewQuestion question = createQuestion(repository, interview, recommendedQuestion, techList);
        Events.raise(new TopicChangedQuestionCreatedEvent(conversationPair.getId(), question.getId()));
    }

    private InterviewQuestion createQuestion(
            InterviewQuestionRepository repository, Interview interview,
            RecommendedQuestion recommendedQuestion, List<TechnicalSubjects> techList
    ) {
        Users users = interview.getUsers();
        JobCategory appliedJob = interview.getAppliedJob();
        String content = recommendedQuestion.question();
        QuestionType type = QuestionConvertor.convert(recommendedQuestion.progress().phase());
        InterviewQuestion question = InterviewQuestion.create(
                repository, content, appliedJob, users,
                type, recommendedQuestion.createdBy()
        );
        question.linkTech(techList);
        return question;
    }
}

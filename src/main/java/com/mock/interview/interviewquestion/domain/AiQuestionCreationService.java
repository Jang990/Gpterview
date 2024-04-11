package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewquestion.application.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
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

    private InterviewQuestion createQuestion(
            InterviewQuestionRepository repository, Interview interview,
            RecommendedQuestion recommendedQuestion, List<TechnicalSubjects> techList
    ) {
        Users users = interview.getUsers();
        String content = recommendedQuestion.question();
        QuestionType type = QuestionConvertor.convert(recommendedQuestion.progress().phase());

        InterviewQuestion question = InterviewQuestion.create(
                repository, content, users,
                type, recommendedQuestion.createdBy()
        );
        question.linkJob(interview.getCategory(), interview.getPosition());
        question.linkTech(techList);
        return question;
    }
}

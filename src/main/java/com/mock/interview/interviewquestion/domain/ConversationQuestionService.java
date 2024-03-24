package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.TopicChangedQuestionCreatedEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationQuestionService {
    public InterviewQuestion save(
            InterviewQuestionRepository repository, long conversationPairId, Interview interview,
            RecommendedQuestion recommendedQuestion, List<TechnicalSubjects> techList
    ) {
        InterviewQuestion question = InterviewQuestion.createConversationQuestion(
                repository, interview.getUsers(), interview.getAppliedJob(),
                recommendedQuestion, techList
        );
        Events.raise(new ConversationQuestionCreatedEvent(conversationPairId, question.getId()));
        return question;
    }

    public void saveTopicChangedQuestion(
            InterviewQuestionRepository repository,
            Interview interview, InterviewConversationPair conversationPair,
            RecommendedQuestion recommendedQuestion, List<TechnicalSubjects> techList
    ) {
        InterviewQuestion question = InterviewQuestion.createConversationQuestion(
                repository, interview.getUsers(), interview.getAppliedJob(),
                recommendedQuestion, techList
        );

        Events.raise(new TopicChangedQuestionCreatedEvent(conversationPair.getId(), question.getId()));
    }
}

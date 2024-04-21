package com.mock.interview.interviewconversationpair.application.helper;

import com.mock.interview.interviewanswer.application.helper.AnswerConvertor;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.interviewconversationpair.presentation.dto.PairStatusForView;
import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class ConversationConvertor {
    private ConversationConvertor() {}

    @Nonnull
    public static ConversationContentDto convert(
            @Nonnull InterviewConversationPair conversationPair,
            @Nullable InterviewQuestion question,
            @Nullable InterviewAnswer answer
    ) {
        return new ConversationContentDto(
                convert(conversationPair),
                QuestionConvertor.convertInterviewQuestion(question),
                AnswerConvertor.convert(answer)
        );
    }

    @Nonnull
    public static InterviewConversationPairDto convert(@Nonnull InterviewConversationPair conversationPair) {
        return new InterviewConversationPairDto(conversationPair.getId(), convertStatus(conversationPair));
    }

    @Nonnull
    public static PairStatusForView convertStatus(@Nonnull InterviewConversationPair conversationPair) {
        return switch (conversationPair.getStatus()) {
            case START ->  throw new IllegalArgumentException("서버 데이터 처리 문제");
            case WAITING_QUESTION -> PairStatusForView.EMPTY;
            case WAITING_ANSWER -> {
                if(conversationPair.getQuestion() == null)
                    yield PairStatusForView.RESTART_RECOMMENDED;
                yield PairStatusForView.ONLY_QUESTION;
            }
            case COMPLETED -> PairStatusForView.COMPLETED;
        };
    }
}

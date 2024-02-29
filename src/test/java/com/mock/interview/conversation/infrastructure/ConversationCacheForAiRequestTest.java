package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@Transactional
class ConversationCacheForAiRequestTest {

    @Autowired ConversationCacheForAiRequest repo;
//    @Test
    void findCurrentConversation() {
        MessageHistory currentConversation = repo.findCurrentConversation(109);
        System.out.println(currentConversation);
    }
}
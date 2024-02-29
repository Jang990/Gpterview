package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest
@Transactional
class ConversationCacheForAiRequestTest {

    @Autowired
    ConversationCacheForAiRequest repo;
    
//    @Test
    void findCurrentConversation() {
        MessageHistory currentConversation = repo.findCurrentConversation(109);
        System.out.println(currentConversation);
    }
}
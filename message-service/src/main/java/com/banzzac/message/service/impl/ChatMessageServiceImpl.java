package com.banzzac.message.service.impl;


import com.banzzac.message.document.ChatMessage;
//import com.banzzac.message.repository.ChatMessageRepository;
import com.banzzac.message.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {
//    private final MongoTemplate mongoTemplate;
//    private final ChatMessageRepository chatMessageRepository;

//    public ChatMessageServiceImpl(MongoTemplate mongoTemplate, ChatMessageRepository chatMessageRepository) {
//    public ChatMessageServiceImpl(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//        this.chatMessageRepository = chatMessageRepository;
//    }

    public void mongoInsert() {
        ChatMessage chatMessage = new ChatMessage("testMessageId", 1L);

    }

}

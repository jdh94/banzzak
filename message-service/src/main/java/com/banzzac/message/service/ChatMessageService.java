package com.banzzac.message.service;

import com.banzzac.message.document.ChatMessage;
//import com.banzzac.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


public interface ChatMessageService {
    Mono<ChatMessage> messageInsertService();

    Mono<ChatMessage> getMessage();

    Mono<ChatMessage> saveChatMessage();
}

/*
public class ChatMessageService {
    private final MongoTemplate mongoTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }



    public void mongoInsert() {
        ChatMessage chatMessage = new ChatMessage("testMessageId", 1L);
        mongoTemplate.insert(chatMessage);
    }
}
*/
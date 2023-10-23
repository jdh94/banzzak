package com.banzzac.message.service.impl;


import com.banzzac.message.document.ChatMessage;
//import com.banzzac.message.repository.ChatMessageRepository;
import com.banzzac.message.repository.ChatMessageRepository;
import com.banzzac.message.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {
//    private final MongoTemplate mongoTemplate;
//    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    ReactiveMongoTemplate template;
//    public ChatMessageServiceImpl(MongoTemplate mongoTemplate, ChatMessageRepository chatMessageRepository) {
//    public ChatMessageServiceImpl(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//        this.chatMessageRepository = chatMessageRepository;
//    }

    public Mono<ChatMessage> messageInsertService() {
        Mono<ChatMessage> chatMessage = chatMessageRepository.save(new ChatMessage("testMessageId", "room1"));
        //Mono<ChatMessage> chatMessage = new Mono<ChatMessage>("testMessageId2", 1L);
        //ChatMessage chatMessage = new ChatMessage("testMessageId");

        return template.save(chatMessage);
    }

    public Mono<ChatMessage> getMessage(){
//        Mono<ChatMessage> chatMessage = chatMessageRepository.findById("testMessageId");
//        return chatMessage;
        //ChatMessage chatMessage = chatMessageRepository.findById("testMessageId");

        Mono<ChatMessage> chatMessage = chatMessageRepository.findById("testMessageId");
        return template.findById("testMessageId", ChatMessage.class);
    }

    @Override
    public Mono<ChatMessage> saveChatMessage()  {
        return Mono.just(new ChatMessage())

                .flatMap(chatMessage -> {
                    return Mono.just(ChatMessage.builder()
                            .messageId("testMessageId")
                            .roomId("room1")
                            .build());
                })
                .flatMap(chatMessageRepository::save);
    }

}








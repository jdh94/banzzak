//package com.banzzac.message.service.impl;
//
//import com.banzzac.message.document.ChatMessage;
//import com.banzzac.message.repository.ChatMessageRepository;
//import com.banzzac.message.service.ChatMessageService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//@Service
//@Slf4j
//public class TestServiceImpl implements ChatMessageService {
//    @Autowired
//    private ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    ReactiveMongoTemplate template;
//
//    public Mono<ChatMessage> messageInsertService() {
//        Mono<ChatMessage> chatMessage = chatMessageRepository.save(new ChatMessage("testMessageId", "testRoomId"));
//        return template.save(chatMessage);
//    }
//
//    public Mono<ChatMessage> getMessage(){
//        Mono<ChatMessage> chatMessage = chatMessageRepository.findById("testMessageId");
//        return template.findById("testMessageId", ChatMessage.class);
//    }
//
//    @Override
//    public Mono<ChatMessage> saveChatMessage()  {
//        return Mono.just(new ChatMessage())
//                .flatMap(chatMessage -> {
//                    return Mono.just(ChatMessage.builder()
//                            .messageId("testMessageId")
//                            .roomId("testRoomId")
//                            .build());
//                })
//                .flatMap(chatMessageRepository::save);
//    }
//
//}
//
//
//
//
//
//
//

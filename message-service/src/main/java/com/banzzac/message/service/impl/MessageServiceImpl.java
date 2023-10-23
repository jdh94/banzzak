package com.banzzac.message.service.impl;

import com.banzzac.message.document.Message;
import com.banzzac.message.repository.MessageRepository;
import com.banzzac.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository MessageRepository;

    @Autowired
    ReactiveMongoTemplate template;

    public Mono<Message> messageInsertService() {
        Mono<Message> message = MessageRepository.save(new Message("testMessageId", "testRoomId"));
        return template.save(message);
    }

    public Mono<Message> getMessage(){
        Mono<Message> message = MessageRepository.findById("testMessageId");
        return template.findById("testMessageId", Message.class);
    }

    @Override
    public Mono<Message> saveMessage()  {
        return Mono.just(new Message())
                .flatMap(Message -> {
                    return Mono.just(Message.builder()
                            .messageId("testMessageId")
                            .roomId("testRoomId")
                            .build());
                })
                .flatMap(MessageRepository::save);
    }

}








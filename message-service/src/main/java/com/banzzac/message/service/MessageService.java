package com.banzzac.message.service;

import com.banzzac.message.document.Message;
import reactor.core.publisher.Mono;

public interface MessageService {
    Mono<Message> messageInsertService();

    Mono<Message> getMessage();

    Mono<Message> saveMessage();
}

package com.banzzac.message.repository;

//import com.banzzac.message.document.ChatMessage;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.banzzac.message.document.ChatMessage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMessageRepository extends ReactiveCrudRepository<ChatMessage, String> {
}

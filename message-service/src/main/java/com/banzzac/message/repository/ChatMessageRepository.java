package com.crewa.message.repository;

import com.crewa.message.document.ChatMessage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatMessageRepository extends ReactiveCrudRepository<ChatMessage, String> {
}

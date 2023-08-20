package com.crewa.message.service;

import com.crewa.message.document.ChatMessage;
import reactor.core.publisher.Mono;

public interface ChatMessageService {
    Mono<ChatMessage> saveChatMessage(Long roomId, String sender, String recipient, String content, String time);
}

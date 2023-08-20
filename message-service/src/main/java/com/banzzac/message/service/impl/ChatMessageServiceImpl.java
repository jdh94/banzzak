package com.crewa.message.service.impl;

import com.crewa.message.constant.MessageStatus;
import com.crewa.message.document.ChatMessage;
import com.crewa.message.repository.ChatMessageRepository;
import com.crewa.message.service.ChatMessageService;
import com.crewa.utils.common.exception.InvalidDataException;
import com.crewa.utils.common.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public Mono<ChatMessage> saveChatMessage(Long roomId, String sender, String recipient, String content, String time) {
        return Mono.just(new ChatMessage())
                .flatMap(chatMessage -> {
                    if(StringUtils.isEmpty(content)){
                        return Mono.error(new InvalidDataException(ErrorCode.MESSAGE_EMPTY_CONTENT));
                    }

                    if(StringUtils.isEmpty(sender) || StringUtils.isEmpty(recipient)){
                        return Mono.error(new InvalidDataException(ErrorCode.MESSAGE_EMPTY_SENDER_RECIPIENT));
                    }

                    if(StringUtils.isEmpty(time)){
                        return Mono.error(new InvalidDataException(ErrorCode.MESSAGE_EMPTY_TIME));
                    }

                    if(StringUtils.isEmpty(roomId.toString())){
                        return Mono.error(new InvalidDataException(ErrorCode.MESSAGE_EMPTY_ROOM_INFO));
                    }

                  return Mono.just(ChatMessage.builder()
                          .sender(sender)
                          .recipient(recipient)
                          .content(content)
                          .status(MessageStatus.RECEIVED)
                          .time(time)
                          .build());
                })
                .flatMap(chatMessageRepository::save);
    }
}

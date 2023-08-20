package com.banzzac.message.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_message")
public class ChatMessage {

    @Id
    private String messageId;
    @Field(name = "room_id")
    private Long roomId;
    private String sender;
    private String recipient;
    private String content;
    private String time;
    private MessageStatus status;
}

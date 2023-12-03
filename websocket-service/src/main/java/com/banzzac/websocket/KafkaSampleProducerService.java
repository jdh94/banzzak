package com.banzzac.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSampleProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        System.out.println("send message : " +  message);

        // 카프카 토픽, 메세지
        this.kafkaTemplate.send("dev-topic", message);
    }
}

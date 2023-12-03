package com.banzzac.websocket;

import com.banzzac.websocket.config.ServerEndpointConfigurator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@ServerEndpoint(value = "/chat", configurator = ServerEndpointConfigurator.class)
@Service
public class WebSocketChatting {

    @Autowired
    private KafkaSampleProducerService kafkaSampleProducerService;

    private static Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.toString());

        if (CLIENTS.contains(session)) {
            System.out.println("already session connected > " + session);
        } else {
            CLIENTS.add(session);
            System.out.println("new session > " + session);
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        CLIENTS.remove(session);
        System.out.println("session close : " + session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("input message > " + message);

        for (Session client : CLIENTS) {
            System.out.println("test : send Message to client > " + message);

            kafkaSampleProducerService.sendMessage(message);

            client.getBasicRemote().sendText(message);
        }
    }

}

package com.banzzac.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@ServerEndpoint(value = "/chat")
@Service
public class WebSocketChatting {
    private static Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.toString());

        if (CLIENTS.contains(session)) {
            System.out.println("이미 연결된 세션입니다. > " + session);
        } else {
            CLIENTS.add(session);
            System.out.println("새로운 세션입니다. > " + session);
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        CLIENTS.remove(session);
        System.out.println("세션을 닫습니다. : " + session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("입력된 메세지입니다. > " + message);

        // 객체전달
        Map<String, Object> map = new HashMap<>();
        map.put("testKey1", "100");
        map.put("testKey2", "200");


        for (Session client : CLIENTS) {
            System.out.println("메세지를 전달합니다. > " + message);

            client.getBasicRemote().sendText(message);
        }
    }

    public void requestRestAPI(Map map) throws Exception {

        String messageUrl = "http://localhost:9998/message?";
        String query = "&key=" + map.get("testKey1");
        URL url = new URL(messageUrl + query);

        ObjectMapper mapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder();
        BufferedReader br;

        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();

        try {
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            //Request Header 정의
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            //전송방식
            con.setRequestMethod("GET");

            //서버에 연결되는 Timeout 시간 설정
            con.setConnectTimeout(5000);

            //InputStream 읽어 오는 Timeout 시간 설정
            con.setReadTimeout(5000);

            //URLConnection에 대한 doOutput 필드값을 지정된 값으로 설정한다.
            //URL 연결은 입출력에 사용될 수 있다.
            //URL 연결을 출력용으로 사용하려는 경우 DoOutput 플래그를 true로 설정하고,
            //그렇지 않은 경우는 false로 설정해야 한다. 기본값은 false이다.
            con.setDoOutput(true);

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                //mapper.readValue(sb.toString(), new TypeReference<List<Map<String, Object>>>(){});
                System.out.println("data delivered");
                // model.addAttribute("listMap", listMap);
            } else {
                System.out.println("data not delivered");
                //model.addAttribute("error", con.getResponseMessage());
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        // return "test/api.tiles";
    }

}

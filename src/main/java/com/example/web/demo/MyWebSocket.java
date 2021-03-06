package com.example.web.demo;

import com.example.web.demo.enums.methodEnum;
import com.example.web.demo.handler.MessageHandler;
import com.example.web.demo.model.ReceiveTo;
import com.example.web.demo.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @time：
 * @Discription：
 */
@ServerEndpoint("/user")
@Component
@Slf4j
public class MyWebSocket {
    private static Gson gson = new Gson();
    private static Map<User,Session> sessionMap = new ConcurrentHashMap<>();



    @OnOpen
    public void onOpen(Session session){
        //我需要将session和特定的信息绑定起来才行，这里没有接收到东西，应该要去onMessage中接收
        log.info("webSocket连接");
    }

    @OnMessage
    public void onMessage(String message , Session session) throws IOException {
        ReceiveTo receiveFromAndroid = gson.fromJson(message,ReceiveTo.class);
        if (methodEnum.LOGIN.getState() == receiveFromAndroid.getMethod()){
            //将一些信息和session关联在一起
            ReceiveTo<User> receiveAsUser = gson.fromJson(message,new TypeToken<ReceiveTo<User>>(){}.getType());
            if (!sessionMap.containsValue(session)){
                log.info("map中不存在session");
                sessionMap.put(receiveAsUser.getRequestBody(),session);
            }
        }
        //将从移动端得到的信息进行处理
        MessageHandler handler = new MessageHandler();
        //这个方法会将信息发送到对应的客户端中
        String feedBackJson= handler.messageHandler(message,sessionMap);
        if (receiveFromAndroid.getMethod() != methodEnum.CHAT_USER.getState()) {
            session.getBasicRemote().sendText(feedBackJson);
        }
    }

    @OnClose
    public void onClosed(Session sessions) {
        //将这个session从Map中移除
        if (sessionMap.size() == 0) {
            log.info(" ========= 没有人登录，或者只是在登录界面 ============ ");
        } else {
            for (Map.Entry<User, Session> entry : sessionMap.entrySet()) {
                if (entry.getValue() == sessions) {
                    sessionMap.remove(entry.getKey());
                }
            }
            log.info("有session退出了,现在人数为" + sessionMap.size());
        }
    }


}

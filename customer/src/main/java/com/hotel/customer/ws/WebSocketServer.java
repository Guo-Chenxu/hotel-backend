package com.hotel.customer.ws;

import com.hotel.common.constants.HttpCode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint("/cool/watchAC/{userId}")
@Component
public class WebSocketServer {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 用户ID
     */
    private String userId;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebSocket是当前类名
    private static final CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // 顾客端和服务端连接的websocket
    private static final ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    private static final String acUrl = "http://10.29.23.17:29011/api/server/cool/watchAC/%s";

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接, 连接用户id为: {}, 连接总数为: {}", userId, webSockets.size());
            OkHttpClient client = new OkHttpClient.Builder().build();
            String url = String.format(acUrl, userId)
                    .replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url).build();

            WebSocket webSocket = client.newWebSocket(request, new ACWebsocket(userId, null, this));
            webSocketMap.put(this.userId, webSocket);
        } catch (Exception e) {
            log.error("【websocket消息】userId: {}, 链接异常, ", userId, e);
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            WebSocket webSocket = webSocketMap.get(this.userId);
            webSocketMap.remove(this.userId);
            if (webSocket != null) {
                webSocket.close(HttpCode.SUCCESS, "websocket由顾客端主动关闭");
            }
            log.info("【websocket消息】连接断开, userId: {}, 总数为: {}", userId, webSockets.size());
        } catch (Exception e) {
            log.error("【websocket消息】连接断开异常, userId: {}, 总数为: {}, 异常原因: ", userId, webSockets.size(), e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端消息:" + message);
    }

    /**
     * 发送错误时的处理
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因: ", error);
    }


    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        for (WebSocketServer webSocketServer : webSockets) {
            try {
                if (webSocketServer.session.isOpen()) {
                    webSocketServer.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                log.error("【websocket消息】广播消息异常:", e);
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket消息】 userId: {}, 单点消息: {}", userId, message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("【websocket消息】 userId: {}, 单点消息异常: ", userId, e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServer that = (WebSocketServer) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
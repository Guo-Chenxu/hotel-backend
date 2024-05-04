package com.hotel.customer.ws;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 星火websocket通信
 *
 * @author: 郭晨旭
 * @create: 2024-03-17 14:16
 * @version: 1.0
 */
@Slf4j
public class ACWebsocket extends WebSocketListener {
// todo 空调有些问题（但又好像不是问题），ws没有断连，当用户退出登录后房间依旧还是会扣费，但是登录的时候做了判断，避免多个线程

    private String userId;

    private Boolean wsCloseFlag; // 控制ws连接是否关闭

    private WebSocketServer webSocketServer;

    // 构造函数
    public ACWebsocket(String userId, HttpServletResponse response, WebSocketServer webSocketServer) {
        this.userId = userId;
        this.wsCloseFlag = false;
        this.webSocketServer = webSocketServer;
    }

    // 线程来发送参数
    class MyThread extends Thread {
        private WebSocket webSocket;

        public MyThread(WebSocket webSocket) {
            this.webSocket = webSocket;
        }

        @Override
        public void run() {
            try {
                // 等待服务端返回完毕后关闭
                while (!wsCloseFlag) {
                    Thread.sleep(200);
                }
                webSocket.close(1000, "websocket连接关闭");
            } catch (Exception e) {
                log.error("MyThread异常, ", e);
            }
        }
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        MyThread myThread = new MyThread(webSocket);
        myThread.start();
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        webSocketServer.sendOneMessage(userId, text);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                log.error("onFailure code: {}", code);
                log.error("onFailure body: {}", response.body().string());
                if (101 != code) {
                    log.error("connection failed");
                }
            }
        } catch (IOException e) {
            log.error("ws onFailure error: ", e);
        }
    }
}

package com.hotel.customer.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

/**
 * 星火websocket通信
 *
 * @author: 郭晨旭
 * @create: 2024-03-17 14:16
 * @version: 1.0
 */
@Slf4j
public class ACWebsockt extends WebSocketListener {

    private String userId;

    private Boolean wsCloseFlag; // 控制ws连接是否关闭

    private PrintWriter writer; // http返回流，用于sse返回

    private WebSocketServer webSocketServer;

    // 构造函数
    public ACWebsockt(String userId, HttpServletResponse response, WebSocketServer webSocketServer) {
        this.userId = userId;
        this.wsCloseFlag = false;
        this.webSocketServer = webSocketServer;
//        try {
//            this.writer = response.getWriter();
//        } catch (IOException e) {
//            log.error("获取http返回流失败: ", e);
//        }
    }

    // 线程来发送音频与参数
    class MyThread extends Thread {
        private WebSocket webSocket;

        public MyThread(WebSocket webSocket) {
            this.webSocket = webSocket;
        }

        public void run() {
            try {
                // 等待服务端返回完毕后关闭
                while (!wsCloseFlag) {
                    Thread.sleep(200);
                }
                webSocket.close(1000, "");
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
//        String temperature = JSON.parseObject(text, String.class);
//        writer.write("data: " + temperature + "\n\n");
//        writer.flush();
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

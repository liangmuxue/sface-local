package com.ss.sdk.socket;

import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.Client.*;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.Device;
import com.ss.sdk.model.Issue;
import com.ss.sdk.utils.ApplicationContextProvider;
import com.ss.sdk.utils.JedisUtil;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.DigestUtils;

import java.net.URI;
import java.nio.ByteBuffer;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

public class MyWebSocketClientLL extends WebSocketClient {

    private static final Logger logger = LogManager.getLogger(MyWebSocketClientLL.class);

    private JedisUtil jedisUtil = ApplicationContextProvider.getBean(JedisUtil .class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);

    public MyWebSocketClientLL(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        logger.info("建立链接");
        send("oiJ6a0f+7UhEbl+sjads0wu0/fiWaQlchoMY1ODr8O4GmBtRDnn4qEDSglYeStPbXdJd+Ic4Wu7pIzRuSjL8NQ==}");
    }

    @Override
    public void onMessage(String message) {
        logger.info("检测到服务器请求...message：" + message);
    }

    @Override
    public void onError(Exception arg0) {
        arg0.printStackTrace();
        logger.info("客户端发生错误,即将关闭!");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        logger.info("客户端已关闭!");
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            System.out.println(new String(bytes.array(),"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

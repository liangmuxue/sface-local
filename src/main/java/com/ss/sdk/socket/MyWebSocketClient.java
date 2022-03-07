package com.ss.sdk.socket;

import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.Device;
import com.ss.sdk.model.Issue;
import com.ss.sdk.utils.*;
import com.sun.jna.NativeLong;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import javax.annotation.Resource;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyWebSocketClient extends WebSocketClient {

    private static final Logger logger = LogManager.getLogger(MyWebSocketClient.class);

    private JedisUtil jedisUtil = ApplicationContextProvider.getBean(JedisUtil.class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private MyWebSocketLL myWebSocketLL = ApplicationContextProvider.getBean(MyWebSocketLL.class);
    private BaseHttpUtil baseHttpUtil = ApplicationContextProvider.getBean(BaseHttpUtil.class);

    public MyWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        logger.info("开始建立链接...");
    }

    @Override
    public void onMessage(String message) {
        logger.info("检测到云端服务器请求...message：" + message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.parseObject(message);
                String command = jsonObject.getString("command");
                String code = jsonObject.getString("code");
                String productCode = jsonObject.getString("productCode");
                Long time = jsonObject.getLong("time");
                Issue issue = new Issue();
                issue.setProductCode(productCode);
                Device d = new Device();
                d.setProductCode(issue.getProductCode());
                d.setIsDelete(1);
                Device device = deviceMapper.selectOne(d);
                if ("opendoor".equals(command) && productCode != null && !"".equals(productCode)) {
                    if (device.getDeviceType() == 3) {
                        issue.setDeviceId(device.getDeviceId());
                        issue.setIssueTime(time);
                        issue.setCode(code);
                        myWebSocketLL.openDoor(issue);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onError(Exception arg0) {
        arg0.printStackTrace();
        logger.info("客户端发生错误,即将关闭!");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        logger.info("客户端已关闭!");
        reConnect();
    }

    private void reConnect() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60 * 1000);
                    logger.info("开始尝试重新连接云服务器...");
                    MyWebSocketClient client = new MyWebSocketClient(new URI(propertiesUtil.getWebSocketUrl()), new Draft_6455());
                    boolean f = client.connectBlocking();
                    logger.info("connectBlocking: " + f);
                    if (client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                        logger.info("成功链接云服务器!");
                        client.send("{'type':'client','tenantId':'CLIENT_" + propertiesUtil.getTenantId() + "'}");
                        MyWebSocket.client = client;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        SysThreadPool.getThread().execute(thread);
    }
}

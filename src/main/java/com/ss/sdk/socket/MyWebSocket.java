package com.ss.sdk.socket;

import com.ss.sdk.utils.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URI;

/**
 * WebSocket
 * @author 李爽超 chao
 * @create 2019/12/20
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class MyWebSocket implements ApplicationRunner {

    public static MyWebSocketClient client;

    private final static Logger logger = LogManager.getLogger(MyWebSocket.class);
    @Resource
    private PropertiesUtil propertiesUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        MyWebSocketClient client = new MyWebSocketClient( new URI(propertiesUtil.getWebSocketUrl()), new Draft_6455() );
        MyWebSocket.client = client;
        boolean f = client.connectBlocking();
        logger.info("connectBlocking: "+ f);
        //判断连接状态
        if (client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            logger.info("成功链接云端服务器!");
            client.send("{'type':'client','tenantId':'CLIENT_" + propertiesUtil.getTenantId() + "'}");
        }
    }
}

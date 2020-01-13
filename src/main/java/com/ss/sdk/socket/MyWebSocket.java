package com.ss.sdk.socket;

import com.ss.sdk.utils.HttpConstant;
import com.ss.sdk.utils.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.net.URI;

/**
 * WebSocketClient
 * @author 李爽超 chao
 * @create 2019/12/20
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class MyWebSocket implements ApplicationRunner {

    private final static Logger logger = LogManager.getLogger(MyWebSocket.class);
    @Resource
    private PropertiesUtil propertiesUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        MyWebSocketClient client = new MyWebSocketClient( new URI(propertiesUtil.getWebSocketUrl()), new Draft_6455() );
        boolean f = client.connectBlocking();
        logger.info("connectBlocking: "+ f);
        //判断连接状态
        if (client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            logger.info("成功链接云端服务器!");
            client.send(propertiesUtil.getTenantId());
        }
//        String pwd = DigestUtils.md5DigestAsHex(this.propertiesUtil.getPasswordLL().getBytes());
//        String md5 = DigestUtils.md5DigestAsHex((this.propertiesUtil.getUserNameLL() + "Authorization" + pwd).getBytes());
//        MyWebSocketClientLL clientLL = new MyWebSocketClientLL(new URI("ws://192.168.0.165:14305/opensmart/api/1.0/login?user=system"));
//        clientLL.connect();
//        while (!clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//            System.out.println("连接中···请稍后");
//            Thread.sleep(1000);
//        }
        //logger.info("connectBlocking: "+ fLL);
//        if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//            logger.info("成功链接冠林服务器!");
//            clientLL.send("{\"User\":\"system\",\"Verify\":\"e10adc3949ba59abbe56e057f20f883e\"}");
//        }
    }
}

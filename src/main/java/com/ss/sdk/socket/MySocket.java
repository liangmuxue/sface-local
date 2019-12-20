package com.ss.sdk.socket;

import com.ss.sdk.utils.PropertiesUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 连接服务器socket
 * @author 李爽超 chao
 * @create 2019/12/17
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class MySocket implements ApplicationRunner {

    @Resource
    private PropertiesUtil propertiesUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        NioClient nc = new NioClient(this.propertiesUtil.getServerIp(), Integer.parseInt(this.propertiesUtil.getSocketPort()));
//        new Thread(nc).start();
//        ClientCache.clientMap.put("id", nc);
    }
}

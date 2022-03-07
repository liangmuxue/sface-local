package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Device;
import com.ss.sdk.utils.BaseHttpUtil;
import com.ss.sdk.utils.HttpConstant;
import com.ss.sdk.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@Configuration
@EnableScheduling
public class IpJob {

    private Logger logger = LoggerFactory.getLogger(IpJob.class);
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private BaseHttpUtil baseHttpUtil;
    @Resource
    private PropertiesUtil propertiesUtil;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        logger.info("定时任务IpJob已经启动" + new Date().toString());
        List<Device> devices = new ArrayList<>();
        Device device = new Device();
        device.setIsDelete(1);
        List<Device> allDevice = this.deviceMapper.select(device);
        for (Device d: allDevice) {
            if (d.getIp() != null && !"".equals(d.getIp())){
                try {
                    boolean reachable = InetAddress.getByName(d.getIp()).isReachable(1000);
                    if(d.getState() == 0 && reachable){
                        Device temp = new Device();
                        temp.setId(d.getId());
                        temp.setProductCode(d.getProductCode());
                        temp.setState(1);
                        temp.setCameraState(1);
                        devices.add(temp);
                    } else if (d.getState() == 1 && !reachable) {
                        Device temp = new Device();
                        temp.setId(d.getId());
                        temp.setProductCode(d.getProductCode());
                        temp.setState(0);
                        temp.setCameraState(0);
                        devices.add(temp);
                    }
                } catch (IOException e) {
                    this.logger.error("IpJob异常:" + e.toString(), e);
                    e.printStackTrace();
                }
            }
        }
        if (devices.size() > 0){
            Map<String, Object> parm = new HashMap<>();
            parm.put("deviceList", devices);
            String json = JSON.toJSONString(parm);
            baseHttpUtil.httpPost(json, propertiesUtil.getServerHttp() + HttpConstant.DEVICE_STATE);
            for (Device d: devices) {
                this.deviceMapper.updateByPrimaryKeySelective(d);
            }
        }
    }
}

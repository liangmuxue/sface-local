package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Device;
import com.ss.sdk.utils.ApplicationContextProvider;
import com.ss.sdk.utils.BaseHttpUtil;
import com.ss.sdk.utils.HttpConstant;
import com.ss.sdk.utils.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class IpJob implements SimpleJob {

    public static final Log LOG = LogFactory.getLog(IpJob.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private BaseHttpUtil baseHttpUtil = ApplicationContextProvider.getBean(BaseHttpUtil.class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        LOG.info("定时任务IpJob已经启动" + new Date().toString());
        List<Device> devices = new ArrayList<>();
        List<Device> allDevice = IpJob.this.deviceMapper.findAllDevice();
        for (Device device: allDevice) {
            if (device.getIp() != null && !"".equals(device.getIp())){
                try {
                    boolean reachable = InetAddress.getByName(device.getIp()).isReachable(1000);
                    if(device.getState() == 0 && reachable){
                        Device temp = new Device();
                        temp.setId(device.getId());
                        temp.setProductCode(device.getCplatDeviceId());
                        temp.setState(1);
                        temp.setCameraState(1);
                        devices.add(temp);
                    } else if (device.getState() == 1 && !reachable) {
                        Device temp = new Device();
                        temp.setId(device.getId());
                        temp.setProductCode(device.getCplatDeviceId());
                        temp.setState(0);
                        temp.setCameraState(0);
                        devices.add(temp);
                    } else {
                        Device temp = new Device();
                        temp.setId(device.getId());
                        temp.setProductCode(device.getCplatDeviceId());
                        temp.setState(device.getState());
                        temp.setCameraState(device.getState());
                        devices.add(temp);
                    }
                } catch (IOException e) {
                    LOG.error("IpJob异常:" + e.toString(), e);
                    e.printStackTrace();
                }
            }
        }
        if (devices.size() > 0){
            Map<String, Object> parm = new HashMap<>();
            parm.put("deviceList", devices);
            String json = JSON.toJSONString(parm);
            baseHttpUtil.httpPost(json, propertiesUtil.getCplatHttp() + HttpConstant.DEVICE_STATE);
            IpJob.this.deviceMapper.updateDevice(devices);
        }
    }
}

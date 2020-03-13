package com.ss.sdk.job;

import com.ss.sdk.Client.Alarm;
import com.ss.sdk.Client.Basics;
import com.ss.sdk.Client.HCNetSDK;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Device;
import com.ss.sdk.utils.JedisUtil;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备注册
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class MyApplicationRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(MyApplicationRunner.class);
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private JedisUtil jedisUtil;
    @Resource
    private Basics basics;
    @Resource
    private Alarm alarm;
    @Resource
    private PropertiesUtil propertiesUtil;
    public static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    @Override
    public void run(ApplicationArguments var1) {

        logger.info("开始初始化sdk");
        try {
            hCNetSDK.NET_DVR_Init();
        } catch (Exception e) {
            logger.error("初始化sdk失败" + e, e.toString());
            return;
        }
        logger.info("初始化sdk成功");
        List<Device> deviceList = new ArrayList<>();
        if (this.propertiesUtil.getType() == 0){
            //查询所有海康门禁机
            deviceList = this.deviceMapper.findHivAccessDevice();
            //查询所有海康摄像机
            deviceList.addAll(this.deviceMapper.findHivVideoDevice());
        } else if(this.propertiesUtil.getType() == 1){
            //查询所有海康设备
            deviceList = this.deviceMapper.findAllHivDevice();
        }
        //alarm.StartAlarmListen(propertiesUtil.getIp(), Integer.parseInt(propertiesUtil.getPort()));
        for (Device device : deviceList) {
            NativeLong userId = basics.login(device.getIp(), device.getPort(), device.getUserName(), device.getPassword());
            if (userId.intValue() == -1) {
                int error = hCNetSDK.NET_DVR_GetLastError();
                logger.info("设备" + device.getCplatDeviceId() + "注册失败，错误码：" + error);
            } else {
                jedisUtil.set(String.valueOf(device.getCplatDeviceId()), userId);
                int alarmHandle = alarm.SetupAlarmChan(userId);
                if (alarmHandle == -1) {
                    int error = hCNetSDK.NET_DVR_GetLastError();
                    logger.info("设备" + device.getCplatDeviceId() + "布防失败，错误码：" + error);
                } else {
                    logger.info("设备" + device.getCplatDeviceId() + "布防成功");
                }
            }
        }
        ContinueRead.connect();
    }

}

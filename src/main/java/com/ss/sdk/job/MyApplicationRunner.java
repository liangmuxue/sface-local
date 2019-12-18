package com.ss.sdk.job;

import com.ss.sdk.Client.Alarm;
import com.ss.sdk.Client.Basics;
import com.ss.sdk.Client.HCNetSDK;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Device;
import com.ss.sdk.utils.JedisUtil;
import com.ss.sdk.utils.JobProperties;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备注册
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class MyApplicationRunner implements ApplicationRunner {

    public Logger logger = LoggerFactory.getLogger(MyApplicationRunner.class);
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    public JedisUtil jedisUtil;
    @Resource
    private Basics basics;
    @Resource
    private Alarm alarm;
    public static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    @Resource
    private PropertiesUtil propertiesUtil;

    @Override
    public void run(ApplicationArguments var1) {
        hCNetSDK.NET_DVR_Init();
        List<Device> deviceList = this.deviceMapper.findAllDevice();
        alarm.StartAlarmListen(propertiesUtil.getIp(), Integer.parseInt(propertiesUtil.getPort()));
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
    }

}

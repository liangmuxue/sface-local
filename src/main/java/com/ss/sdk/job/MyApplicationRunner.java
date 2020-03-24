package com.ss.sdk.job;

import com.ss.sdk.Client.Alarm;
import com.ss.sdk.Client.Basics;
import com.ss.sdk.Client.HCNetSDK;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Device;
import com.ss.sdk.utils.BaseHttpUtil;
import com.ss.sdk.utils.HttpConstant;
import com.ss.sdk.utils.JedisUtil;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
    @Resource
    private BaseHttpUtil baseHttpUtil;
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
        if (this.propertiesUtil.getType() == 0) {
            deviceList = this.deviceMapper.findAllDevice();
            ContinueRead.connect();
            //查询所有海康门禁机
            //deviceList = this.deviceMapper.findHivAccessDevice();
            //查询所有海康摄像机
            //deviceList.addAll(this.deviceMapper.findHivVideoDevice());
        } else if (this.propertiesUtil.getType() == 1) {
            //查询所有海康设备
            deviceList = this.deviceMapper.findAllHivDevice();
            ContinueRead.connect();
        }
        for (Device device : deviceList) {
            if (device.getDeviceType() == 1) {
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
            } else if (device.getDeviceType() == 4) {
                List<NameValuePair> param1 = new ArrayList<NameValuePair>();
                NameValuePair pair1 = new BasicNameValuePair("pass", device.getPassword());
                NameValuePair pair2 = new BasicNameValuePair("callbackPermissionUrl", "http://" + this.propertiesUtil.getIp() + ":" + this.propertiesUtil.getPort() + "/sface/guanpinPush/getPersonPermission");
                NameValuePair pair3 = new BasicNameValuePair("timeout", "10000");
                param1.add(pair1);
                param1.add(pair2);
                param1.add(pair3);
                String personPermissionResult = this.baseHttpUtil.httpPostUrl("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.GUANPIN_PULL_SET_PERSION_PERMISSION_PARAMETER, param1);
                if (this.baseHttpUtil.guanpinIsResult(personPermissionResult)){
                    logger.info("冠品设备" + device.getCplatDeviceId() + "设置人脸终端从服务器端获取人员权限成功");
                }
                String data = "{" +
                        "\"syncPara\": \"groupId,{groupId}|deviceKey,{sn}|recordId,{id}|idcardNum,{idcardNum}|imgBase64,{photo}|time,{time}|type,{type}|extra,{extra}\"," +
                        "\"syncValidity\": 0," +
                        "\"syncResponseKey\": \"\"," +
                        "\"syncTimeType\": 3," +
                        "\"syncType\": \"x-www-form-urlencoded\"," +
                        "\"syncUrl\": \"http://" + this.propertiesUtil.getIp() + ":" + this.propertiesUtil.getPort() + "/sface/guanpinPush/dataUpload\"" +
                        "}";
                List<NameValuePair> param2 = new ArrayList<NameValuePair>();
                NameValuePair pair4 = new BasicNameValuePair("pass", device.getPassword());
                NameValuePair pair5 = new BasicNameValuePair("data", data);
                param2.add(pair4);
                param2.add(pair5);
                String dataUploadStrResult = this.baseHttpUtil.httpPostUrl("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.GUANPIN_PULL_SET_DATA_UPLOAD_PARAMETER, param2);
                if (this.baseHttpUtil.guanpinIsResult(dataUploadStrResult)){
                    logger.info("冠品设备" + device.getCplatDeviceId() + "设置识别数据上传参数成功");
                }
            }
        }
    }
}

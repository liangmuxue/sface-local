package com.ss.sdk.Client;

import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.utils.ApplicationContextProvider;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

/**
 * 报警布防和监听
 * @author 李爽超 chao
 * @create 2019/12/16
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class Alarm {

    private Logger logger = LoggerFactory.getLogger(Alarm.class);
    @Resource
    private PropertiesUtil propertiesUtil;
    @Resource
    private DeviceMapper deviceMapper;
    //报警布防句柄
    NativeLong lAlarmHandle;
    //报警监听句柄
    NativeLong lListenHandle;
    //报警回调函数实现
    FMSGCallBack fMSFCallBack;
    //报警回调函数实现
    FMSGCallBack_V31 fMSFCallBack_V31;

    /**
     * 布防
     */
    public int SetupAlarmChan(NativeLong userId) {
        if (fMSFCallBack_V31 == null) {
            fMSFCallBack_V31 = new FMSGCallBack_V31();
            Pointer pUser = null;
            if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
                logger.info("设置回调函数失败!");
            }
        }
        HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
        m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
        m_strAlarmInfo.byLevel = 1;
        m_strAlarmInfo.byAlarmInfoType = 1;
        m_strAlarmInfo.write();
        lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(userId, m_strAlarmInfo);
        return lAlarmHandle.intValue();
    }

    /**
     * 撤防
     */
    public boolean CloseAlarmChan(NativeLong userId) {
        //报警撤防
        boolean result = hCNetSDK.NET_DVR_CloseAlarmChan_V30(userId);
        return result;
    }

    /**
     * 启动监听
     */
    public void StartAlarmListen(String m_sListenIP, int iListenPort) {
        Pointer pUser = null;
        if (fMSFCallBack == null) {
            fMSFCallBack = new FMSGCallBack();
        }
        lListenHandle = hCNetSDK.NET_DVR_StartListen_V30(m_sListenIP, (short) iListenPort, fMSFCallBack, pUser);
        int i = hCNetSDK.NET_DVR_GetLastError();
        if (lListenHandle.intValue() < 0) {
            logger.info("启动监听失败");
        } else {
            logger.info("启动监听成功");
        }
    }

    /**
     * 停止监听
     */
    public void StopAlarmListen() {
        if (lListenHandle.intValue() < 0) {
            return;
        }
        if (!hCNetSDK.NET_DVR_StopListen_V30(lListenHandle)) {
            logger.info("停止监听失败");
        } else {
            logger.info("停止监听成功");
        }
    }

    /**
     * 报警信息回调函数
     */
    private class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {
        @Override
        public boolean invoke(NativeLong lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
            AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
            return true;
        }

    }

    /**
     * 报警信息回调函数
     */
    private class FMSGCallBack implements HCNetSDK.FMSGCallBack {
        @Override
        public void invoke(NativeLong lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
            AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
        }
    }

    private void AlarmDataHandle(NativeLong lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        //lCommand是传的报警类型
        switch (lCommand.intValue()) {
            case 0x1132:
                HCNetSDK.NET_DVR_VIDEO_INTERCOM_EVENT videoIntercomEvent = new HCNetSDK.NET_DVR_VIDEO_INTERCOM_EVENT();
                videoIntercomEvent.write();
                Pointer pointer = videoIntercomEvent.getPointer();
                pointer.write(0, pAlarmInfo.getByteArray(0, videoIntercomEvent.size()), 0, videoIntercomEvent.size());
                videoIntercomEvent.read();
                switch (videoIntercomEvent.byEventType) {
                    case 1:
                        Capture capture = new Capture();
                        //人员主键
                        String peopleId= new String(videoIntercomEvent.uEventInfo.struUnlockRecord.byControlSrc).replaceAll("\\p{C}", "");
                        if (peopleId != null && !"".equals(peopleId)){
                            capture.setPeopleId(Integer.valueOf(new String(videoIntercomEvent.uEventInfo.struUnlockRecord.byControlSrc).replaceAll("\\p{C}", "")));
                        }
                        //设备编号
                        capture.setDeviceId(new String(new String(videoIntercomEvent.byDevNumber).replaceAll("\\p{C}", "")));
                        //8为人脸开锁
                        if (videoIntercomEvent.uEventInfo.struUnlockRecord.byUnlockType == 8){
                            capture.setOpendoorMode(1);
                        } else if (videoIntercomEvent.uEventInfo.struUnlockRecord.byUnlockType == 5){
                            capture.setOpendoorMode(2);
                        } else {
                            capture.setOpendoorMode(0);
                        }
                        //开门成功
                        capture.setResultCode(1);
                        FileOutputStream fout;
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        try {
                            String url = propertiesUtil.getCaptureUrl() + "/" + capture.getDeviceId() + "_" + newName + ".jpg";
                            File dir = new File(url);
                            if (!dir.exists()) {
                                try {
                                    boolean newFile = dir.createNewFile();
                                    if (newFile){
                                        logger.info("创建成功");
                                    } else {
                                        logger.info("创建失败");
                                    }
                                } catch (Exception e) {
                                    logger.info("创建文件失败：" + e, e.toString());
                                }
                            }
                            fout = new FileOutputStream(url);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = videoIntercomEvent.uEventInfo.struUnlockRecord.pImage.getByteBuffer(offset, videoIntercomEvent.uEventInfo.struUnlockRecord.dwPicDataLen);
                            byte[] bytes = new byte[videoIntercomEvent.uEventInfo.struUnlockRecord.dwPicDataLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                            capture.setCaptureUrl(url);
                        } catch (IOException e) {
                            logger.info("抓拍照存储异常：" + e, e.toString());
                        }
                        //认证时间
                        capture.setCompareDate(String.valueOf(System.currentTimeMillis()));
                        int result = this.deviceMapper.insertCapture(capture);
                        if (result > 0){
                            logger.info("刷脸认证信息录入成功，设备编号：" + capture.getDeviceId());
                        } else {
                            logger.info("刷脸认证信息录入失败，设备编号：" + capture.getDeviceId());
                        }
                        break;
                    case 2:
                        logger.info("认证类型:2");
                        break;
                    case 3:
                        logger.info("认证类型:3");
                        break;
                    case 5:
                        logger.info("认证类型:5");
                        break;
                    case 6:
                        logger.info("认证类型:6");
                        break;
                }
        }
    }
}

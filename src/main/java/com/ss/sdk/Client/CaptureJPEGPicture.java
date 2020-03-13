package com.ss.sdk.Client;

import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

/**
 * 获取海康设备JPEG图片
 *
 * @author 李爽超 chao
 * @create 2020/02/27
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class CaptureJPEGPicture {

    private Logger logger = LoggerFactory.getLogger(Alarm.class);
    @Resource
    private PropertiesUtil propertiesUtil;

    public String SetupAlarmChan(NativeLong userId) {
        HCNetSDK.NET_DVR_JPEGPARA netDvrJpegpara = new HCNetSDK.NET_DVR_JPEGPARA();
        netDvrJpegpara.wPicQuality = 2;
        netDvrJpegpara.wPicSize = 6;
        String name = String.valueOf(System.currentTimeMillis());
        boolean result = hCNetSDK.NET_DVR_CaptureJPEGPicture(userId, new NativeLong(1), netDvrJpegpara, propertiesUtil.getTempPictureUrl() + name +".jpeg");
        if (result){
            return propertiesUtil.getTempPictureUrl() + name +".jpeg";
        } else {
            int iErr = hCNetSDK.NET_DVR_GetLastError();
            logger.error("海康设备获取图片失败，错误码：" + iErr);
            return null;
        }
    }
}

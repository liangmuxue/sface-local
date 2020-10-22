package com.ss.sdk.service.impl;

import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.CloudwalkCapture;
import com.ss.sdk.model.Device;
import com.ss.sdk.service.ICloudwalkService;
import com.ss.sdk.socket.MyWebSocket;
import com.ss.sdk.utils.Base64Util;
import com.ss.sdk.utils.PropertiesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * com.ss.sdk.service.impl
 *
 * @author 李爽超 chao
 * @create 2020/04/13
 * @email lishuangchao@ss-cas.com
 **/
@Service
public class CloudwalkServiceImpl implements ICloudwalkService {

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private PropertiesUtil propertiesUtil;

    @Override
    public int insertCapture(CloudwalkCapture cloudwalkCapture) throws Exception {

        int resultCode = 0;
        Capture capture = new Capture();
        capture.setDeviceId(cloudwalkCapture.getDeviceNo());
        capture.setCompareDate(String.valueOf(System.currentTimeMillis()));
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newName = sf.format(new Date());
        if (cloudwalkCapture.getSpotImgPath() != null) {
            String url = this.propertiesUtil.getCaptureUrl() + "/" + capture.getDeviceId() + "_" + newName + ".jpg";
            Base64Util.saveImg(cloudwalkCapture.getSpotImgPath(), url);
            capture.setCaptureUrl(url);
            Device d = new Device();
            d.setDeviceId(capture.getDeviceId());
            Device device = this.deviceMapper.findDevice(d);
            if (MyWebSocket.client != null) {
                MyWebSocket.client.send("{'type':'normal','base64':'" + cloudwalkCapture.getSpotImgPath() + "'," + "'deviceId':'" + device.getCplatDeviceId()
                        + "','captureTime':'" + capture.getCompareDate() + "','tenantId':'" + this.propertiesUtil.getTenantId() + "'}");
            }
        } else if (cloudwalkCapture.getPanoramaPath() != null) {

            String url = this.propertiesUtil.getCaptureUrl() + "/" + capture.getDeviceId() + "_" + newName + "_full.jpg";
            Base64Util.saveImg(cloudwalkCapture.getPanoramaPath(), url);
            capture.setCaptureFullUrl(url);
        }
        List<Capture> commonCaptureList = this.deviceMapper.findCloudwalk(capture);
        if (commonCaptureList.size() > 0) {
            capture.setId(commonCaptureList.get(0).getId());
            resultCode = this.deviceMapper.updateCapture(capture);
        } else {
            capture.setOpendoorMode(-1);
            capture.setCreateTime(String.valueOf(System.currentTimeMillis()));
            resultCode = this.deviceMapper.insertCapture(capture);
        }
        return resultCode;
    }

}

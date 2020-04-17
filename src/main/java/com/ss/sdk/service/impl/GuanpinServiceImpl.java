package com.ss.sdk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.job.ContinueRead;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.Device;
import com.ss.sdk.model.GuanpinRequest;
import com.ss.sdk.service.IGuanpinService;
import com.ss.sdk.socket.MyWebSocket;
import com.ss.sdk.utils.Base64Util;
import com.ss.sdk.utils.JedisUtil;
import com.ss.sdk.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * com.ss.sdk.service.impl
 *
 * @author 李爽超 chao
 * @create 2020/03/17
 * @email lishuangchao@ss-cas.com
 **/
@Service
public class GuanpinServiceImpl implements IGuanpinService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private PropertiesUtil propertiesUtil;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 冠品新增抓拍信息
     * @param guanpinRequest
     * @return
     * @throws Exception
     */
    @Override
    public int dataUpload(GuanpinRequest guanpinRequest) throws Exception {
        Capture capture = new Capture();
        String bodyTemp = null;
        JSONObject jsobj = null;
        if (null != guanpinRequest.getExtra()) {
            jsobj = JSONObject.parseObject(guanpinRequest.getExtra());
            if(jsobj != null){
                bodyTemp = jsobj.get("bodyTemp").toString();
                capture.setTemp(Double.valueOf(bodyTemp));
                if (capture.getTemp() > 37.5){
                    capture.setTempState(1);
                } else {
                    capture.setTempState(0);
                }
            }
        }
        if ("STRANGER".equals(guanpinRequest.getIdcardNum())){
            capture.setResultCode(0);
        } else {
            capture.setResultCode(1);
            capture.setPeopleId(guanpinRequest.getIdcardNum());
            if (capture.getTempState() != null) {
                this.jedisUtil.set(guanpinRequest.getDeviceKey() + "-" + guanpinRequest.getIdcardNum(), capture.getTempState(), 3000);
            }
        }
        capture.setDeviceId(guanpinRequest.getDeviceKey());
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newName = sf.format(new Date());
        String url = this.propertiesUtil.getCaptureUrl() + "/" + capture.getDeviceId() + "_" + newName + ".jpg";
        Base64Util.saveImg(guanpinRequest.getImgBase64(), url);
        capture.setCaptureUrl(url);
        capture.setCompareDate(guanpinRequest.getTime());
        capture.setOpendoorMode(1);
        capture.setCreateTime(String.valueOf(System.currentTimeMillis()));
        int result = this.deviceMapper.insertCapture(capture);
        Device d = new Device();
        d.setDeviceId(capture.getDeviceId());
        Device device = this.deviceMapper.findDevice(d);
        if (capture.getTempState() != null && capture.getTempState() == 1) {
            MyWebSocket.client.send("{'type':'tempAlarm','peopleId':'" + capture.getPeopleId() + "','temp':'" + capture.getTemp() + "','base64':'" + guanpinRequest.getImgBase64() + "'," + "'deviceId':'" + device.getCplatDeviceId()
                    + "','captureTime':'" + capture.getCompareDate() + "','tenantId':'" + this.propertiesUtil.getTenantId() + "'}");
        } else {
            MyWebSocket.client.send("{'type':'normal','peopleId':'" + capture.getPeopleId() + "','temp':'" + capture.getTemp() + "','base64':'" + guanpinRequest.getImgBase64() + "'," + "'deviceId':'" + device.getCplatDeviceId()
                    + "','captureTime':'" + capture.getCompareDate() + "','tenantId':'" + this.propertiesUtil.getTenantId() + "'}");
        }
        return result;
    }

    /**
     * 冠品人员权限获取
     * @param guanpinRequest
     * @return
     */
    @Override
    public int getPersonPermission(GuanpinRequest guanpinRequest) throws Exception {
        int personPermission = 0;
        int i = 0;
        while (i <= 5){
            if(this.jedisUtil.hasKey(guanpinRequest.getDeviceKey() + "-" + guanpinRequest.getIdcardNum())){
                int tempState = (int)this.jedisUtil.get(guanpinRequest.getDeviceKey() + "-" + guanpinRequest.getIdcardNum());
                if (tempState == 0){
                    personPermission = 1;
                }
                break;
            }
            i++;
            Thread.sleep(500);
        }
//        Thread.sleep(2000);
//        personPermission = this.deviceMapper.getPersonPermission(guanpinRequest);
        return personPermission;
    }
}

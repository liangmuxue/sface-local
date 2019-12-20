package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传抓拍信息定时任务
 * @author 李爽超 chao
 * @create 2019/12/16
 * @email lishuangchao@ss-cas.com
 **/
public class CaptureDataJob implements SimpleJob {

    private Logger logger = LoggerFactory.getLogger(CaptureDataJob.class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private BaseHttpUtil baseHttpUtil = ApplicationContextProvider.getBean(BaseHttpUtil.class);

    @Override
    public void execute(ShardingContext shardingContext) {

        String maxTime = this.deviceMapper.findMaxTime();
        List<Capture> captureList = this.deviceMapper.findCaptureList();
        if (captureList.size() > 0){
            for (Capture capture: captureList) {
//                InputStream in = null;
//                byte[] data = null;
//                try {
//                    in = new FileInputStream(capture.getCaptureUrl());
//                    data = new byte[in.available()];
//                    String encodeToString = Base64Utils.encodeToString(data);
//                    capture.setSpotImgPath(encodeToString);
//                    in.read(data);
//                    in.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                InputStream in = null;
                byte[] data = null;
                if(capture.getCaptureUrl() != null && !"".equals(capture.getCaptureUrl())) {
                    try {
                        File dir = new File(capture.getCaptureUrl());
                        if (dir.exists()) {
                            in = new FileInputStream(capture.getCaptureUrl());
                            data = new byte[in.available()];
                            in.read(data);
                            in.close();
                            String encodeToString = Base64Utils.encodeToString(data);
                            capture.setSpotImgPath(encodeToString);
                        }
                    } catch (Exception e) {
                        logger.info("获取图片base64失败：" + e, e.toString());
                    }
                }
            }
            Map<String, Object> parm = new HashMap<>();
            parm.put("captureList", captureList);
            String json = JSON.toJSONString(parm);
            String resultString = baseHttpUtil.httpPost(json, propertiesUtil.getCplatHttp() + HttpConstant.UPLOAD_CAPTURE_LIST);
            String code = null;
            JSONObject faceJson = null;
            if (null != resultString) {
                faceJson = JSONObject.parseObject(resultString);
                code = faceJson.get("code").toString();
            }
            if ("00000000".equals(code)) {
                this.deviceMapper.updateTime(maxTime);
            }
        }
    }
}

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
import java.util.Date;
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
        logger.info("定时任务CaptureDataJob已经启动" + new Date().toString());
        List<Capture> commonCaptureList = this.deviceMapper.findCommonCaptureList(new Capture());
        logger.info("获取common内容" + commonCaptureList.toString());
        long maxCommonTime = 0;
        //String maxCommonTime = this.deviceMapper.findCommonMaxTime();
        if (commonCaptureList.size() > 0){
            for (Capture capture: commonCaptureList) {
                if (Long.parseLong(capture.getCreateTime()) > maxCommonTime) {
                    maxCommonTime = Long.parseLong(capture.getCreateTime());
                }
                if(capture.getCaptureUrl() != null && !"".equals(capture.getCaptureUrl())) {
                    String imageBase64 = Base64Util.localBase64(capture.getCaptureUrl());
                    capture.setSpotImgPath(imageBase64);
                }
                if(capture.getCaptureFullUrl() != null && !"".equals(capture.getCaptureFullUrl())) {
                    String imageBase64 = Base64Util.localBase64(capture.getCaptureFullUrl());
                    capture.setPanoramaPath(imageBase64);
                }
            }
            Map<String, Object> parm = new HashMap<>();
            parm.put("captureList", commonCaptureList);
            String json = JSON.toJSONString(parm);
            baseHttpUtil.httpPost(json, propertiesUtil.getCplatHttp() + HttpConstant.UPLOAD_COMMON_CAPTURE_LIST);
            this.deviceMapper.updateCommonTime(String.valueOf(maxCommonTime));
        }
        List<Capture> remoteCaptureList = this.deviceMapper.findRemoteCaptureList();
        logger.info("获取remote内容" + remoteCaptureList.toString());
        //String maxRemoteTime = this.deviceMapper.findRemoteMaxTime();
        long maxRemoteTime = 0;
        if (remoteCaptureList.size() > 0){
            for (Capture capture: remoteCaptureList) {
                if (Long.parseLong(capture.getCreateTime()) > maxRemoteTime) {
                    maxRemoteTime = Long.parseLong(capture.getCreateTime());
                }
                if(capture.getCaptureUrl() != null && !"".equals(capture.getCaptureUrl())) {
                    String imageBase64 = Base64Util.localBase64(capture.getCaptureUrl());
                    capture.setSpotImgPath(imageBase64);
                }
            }
            Map<String, Object> parm = new HashMap<>();
            parm.put("captureList", remoteCaptureList);
            String json = JSON.toJSONString(parm);
            baseHttpUtil.httpPost(json, propertiesUtil.getCplatHttp() + HttpConstant.UPLOAD_REMOTE_CAPTURE_LIST);
            this.deviceMapper.updateRemoteTime(String.valueOf(maxRemoteTime));
        }
    }
}

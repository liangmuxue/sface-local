package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.ss.sdk.mapper.CaptureMapper;
import com.ss.sdk.mapper.CaptureTimeMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.CaptureTime;
import com.ss.sdk.utils.Base64Util;
import com.ss.sdk.utils.BaseHttpUtil;
import com.ss.sdk.utils.HttpConstant;
import com.ss.sdk.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * 上传抓拍信息定时任务
 * @author 李爽超 chao
 * @create 2019/12/16
 * @email lishuangchao@ss-cas.com
 **/
@Configuration
@EnableScheduling
public class VisitorCaptureDataJob {

    private Logger logger = LoggerFactory.getLogger(VisitorCaptureDataJob.class);
    @Resource
    private PropertiesUtil propertiesUtil;
    @Resource
    private BaseHttpUtil baseHttpUtil;
    @Resource
    private CaptureMapper captureMapper;
    @Resource
    private CaptureTimeMapper captureTimeMapper;

    @Scheduled(cron = " 0 0/1 * * * ?")
    public void execute() {
        logger.info("定时任务CaptureDataJob已经启动" + new Date().toString());
        CaptureTime captureTime = new CaptureTime();
        captureTime.setType(2);
        captureTime = this.captureTimeMapper.selectOne(captureTime);
        //上次同步最新时间
        Long time = captureTime == null ? 0L : captureTime.getTime();
        //（要改）这里要加个类型
        Example example = new Example(Capture.class);
        //如果是访客people_id后面要加v
        example.createCriteria().andGreaterThan("compareDate", time).andLike("peopleId", "V%");
        //本次要同步数据
        List<Capture> captureList = this.captureMapper.selectByExample(example);
        if (captureList.size() > 0){
            //本次最新数据时间
            long maxTime = 0;
            captureList.forEach(e -> e.setPeopleId(StringUtils.substringAfter(e.getPeopleId(), "V")));
            List<Capture> captures = new ArrayList<>();
            for (Capture capture: captureList) {
                if (capture.getCreateTime() > maxTime) {
                    maxTime = capture.getCreateTime();
                }
                if(capture.getCaptureUrl() != null && !"".equals(capture.getCaptureUrl())) {
                    String imageBase64 = Base64Util.localBase64(capture.getCaptureUrl());
                    capture.setCaptureBase64(imageBase64);
                }
                if(capture.getCaptureFullUrl() != null && !"".equals(capture.getCaptureFullUrl())) {
                    String imageBase64 = Base64Util.localBase64(capture.getCaptureFullUrl());
                    capture.setFullBase64(imageBase64);
                }
                captures.add(capture);
            }

            //拆分成每100条一组
            List<List<Capture>> result = new ArrayList<List<Capture>>();
            int sourceSize = captures.size();
            int size = (captures.size() / 100) + 1;
            for (int i = 0; i < size; i++) {
                List<Capture> subset = new ArrayList<Capture>();
                for (int j = i * 100; j < (i + 1) * 100; j++) {
                    if (j < sourceSize) {
                        subset.add(captures.get(j));
                    }
                }
                result.add(subset);
            }
            //同步到云端
            for (List<Capture> cs: result) {
                Map<String, Object> map = new HashMap<>();
                map.put("captures", cs);
                String json = JSON.toJSONString(map);
                baseHttpUtil.httpPost(json, this.propertiesUtil.getServerHttp() + HttpConstant.UPLOAD_VISITOR_CAPTURE_LIST);
            }
            captureTime.setTime(maxTime);
            //修改同步时间
            this.captureTimeMapper.updateByPrimaryKeySelective(captureTime);
        }
    }
}

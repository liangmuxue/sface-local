package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.ss.sdk.Client.CardCfg;
import com.ss.sdk.Client.FaceParamCfg;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Issue;
import com.ss.sdk.model.WhiteList;
import com.ss.sdk.utils.*;
import com.sun.jna.NativeLong;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下发定时任务
 * @author 李爽超 chao
 * @create 2019/12/12
 * @email lishuangchao@ss-cas.com
 **/
public class GetIssueDataJob implements SimpleJob {

    private JedisUtil jedisUtil = ApplicationContextProvider.getBean(JedisUtil.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private BaseHttpUtil baseHttpUtil = ApplicationContextProvider.getBean(BaseHttpUtil.class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    private CardCfg cardCfg = ApplicationContextProvider.getBean(CardCfg.class);
    private FaceParamCfg faceParamCfg = ApplicationContextProvider.getBean(FaceParamCfg.class);

    @Override
    public void execute(ShardingContext shardingContext) {

        List<WhiteList> whiteList = this.deviceMapper.findWhiteList();
        Map<String, Object> parm = new HashMap<>();
        parm.put("whiteList", whiteList);
        String json = JSON.toJSONString(parm);
        String faceResultString = baseHttpUtil.httpPost(json, propertiesUtil.getCplatHttp() + HttpConstant.FACE_LIST);
        String faceCode = null;
        JSONObject faceJson = null;
        if (null != faceResultString) {
            faceJson = JSONObject.parseObject(faceResultString);
            faceCode = faceJson.get("code").toString();
        }
        if (null != faceJson && "00000000".equals(faceCode)) {
            JSONArray data = faceJson.getJSONArray("data");
            List<Issue> issues = JSONObject.parseArray(data.toJSONString(), Issue.class);
//            //初始化一个map
//            Map<String, List<Issue>> map = new HashMap<>();
//            for (Issue issue : issues) {
//                String key = String.valueOf(issue.getDeviceId());
//                if (map.containsKey(key)) {
//                    //map中存在以此id作为的key，将数据存放当前key的map中
//                    map.get(key).add(issue);
//                } else {
//                    //map中不存在以此id作为的key，新建key用来存放数据
//                    List<Issue> issueList = new ArrayList<>();
//                    issueList.add(issue);
//                    map.put(key, issueList);
//                }
//            }
            for (Issue issue : issues) {
                cardCfg.setCardInfo((NativeLong) jedisUtil.get(String.valueOf(issue.getProductCode())), issue);
                if (issue.getTaskType() != -1) {
                    faceParamCfg.jBtnSetFaceCfg((NativeLong) jedisUtil.get(String.valueOf(issue.getProductCode())), issue);
                }
            }
            List<Issue> list = this.deviceMapper.findIssueList();
            Map<String, Object> parmIssue = new HashMap<>();
            parmIssue.put("statusList", list);
            String jsonIssue = JSON.toJSONString(parmIssue);
            String httpPost = this.baseHttpUtil.httpPost(jsonIssue, propertiesUtil.getCplatHttp() + HttpConstant.FACE_LIST_RESULT);
            String code = null;
            JSONObject resultJson = null;
            if (null != httpPost) {
                resultJson = JSONObject.parseObject(faceResultString);
                code = resultJson.get("code").toString();
            }
            if ("00000000".equals(code) && list.size() > 0) {
                this.deviceMapper.updateIssue(list);
            }
        }
    }
}

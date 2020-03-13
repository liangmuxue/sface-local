package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.ss.sdk.Client.CardCfg;
import com.ss.sdk.Client.FaceParamCfg;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Device;
import com.ss.sdk.model.Issue;
import com.ss.sdk.model.WhiteList;
import com.ss.sdk.socket.MyWebSocketLL;
import com.ss.sdk.utils.*;
import com.sun.jna.NativeLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
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

    private Logger logger = LoggerFactory.getLogger(CaptureDataJob.class);
    private JedisUtil jedisUtil = ApplicationContextProvider.getBean(JedisUtil.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private BaseHttpUtil baseHttpUtil = ApplicationContextProvider.getBean(BaseHttpUtil.class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    private CardCfg cardCfg = ApplicationContextProvider.getBean(CardCfg.class);
    private FaceParamCfg faceParamCfg = ApplicationContextProvider.getBean(FaceParamCfg.class);
    private MyWebSocketLL myWebSocketLL = ApplicationContextProvider.getBean(MyWebSocketLL.class);

    @Override
    public void execute(ShardingContext shardingContext) {

        logger.info("定时任务GetIssueDataJob已经启动" + new Date().toString());
        //查询白名单
        List<WhiteList> whiteList = this.deviceMapper.findWhiteList();
        Map<String, Object> parm = new HashMap<>();
        parm.put("whiteList", whiteList);
        String json = JSON.toJSONString(parm);
        //请求云端下发接口
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
            for (Issue issue : issues) {
                //查找设备
                Device device = this.deviceMapper.findDevice(issue);
                if(device == null){
                    continue;
                }
                if (device.getDeviceType() == 1) {
                    //海康设备
                    //下发卡
                    cardCfg.setCardInfo((NativeLong) jedisUtil.get(String.valueOf(issue.getProductCode())), issue);
                    if (issue.getTaskType() != -1) {
                        //下发人脸
                        faceParamCfg.jBtnSetFaceCfg((NativeLong) jedisUtil.get(String.valueOf(issue.getProductCode())), issue);
                    }
                } else if (device.getDeviceType() == 3) {
                    //冠林设备
                    if (issue.getTaskType() == -1){
                        issue.setDeviceId(device.getDeviceId().substring(0, 4) + "0001");
                        //删除住户
                        myWebSocketLL.tenementQuery(issue);
                    } else {
                        issue.setDeviceId(device.getDeviceId().substring(0, 4) + "0001");
                        //新增住户
                        myWebSocketLL.tenementAdd(issue);
                    }
                }
            }
            List<Issue> list = this.deviceMapper.findIssueList();
            Map<String, Object> parmIssue = new HashMap<>();
            parmIssue.put("statusList", list);
            String jsonIssue = JSON.toJSONString(parmIssue);
            //请求云端下发同步接口
            String httpPost = this.baseHttpUtil.httpPost(jsonIssue, propertiesUtil.getCplatHttp() + HttpConstant.FACE_LIST_RESULT);
            String code = null;
            JSONObject resultJson = null;
            if (null != httpPost) {
                resultJson = JSONObject.parseObject(faceResultString);
                code = resultJson.get("code").toString();
            }
            if ("00000000".equals(code) && list.size() > 0) {
                //修改本地同步状态
                this.deviceMapper.updateIssue(list);
            }
        }
    }
}

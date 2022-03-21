package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.mapper.IssueMapper;
import com.ss.sdk.mapper.WhiteListMapper;
import com.ss.sdk.model.Device;
import com.ss.sdk.model.Issue;
import com.ss.sdk.model.WhiteList;
import com.ss.sdk.socket.MyWebSocketLL;
import com.ss.sdk.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class GetIssueDataJob {

    private Logger logger = LoggerFactory.getLogger(GetIssueDataJob.class);
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private WhiteListMapper whiteListMapper;
    @Resource
    private IssueMapper issueMapper;
    @Resource
    private BaseHttpUtil baseHttpUtil;
    @Resource
    private PropertiesUtil propertiesUtil;
    @Resource
    private MyWebSocketLL myWebSocketLL;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {

        try {
            this.logger.info("定时任务GetIssueDataJob已经启动" + new Date().toString());

            //查询白名单
            List<WhiteList> whiteList = this.whiteListMapper.selectAll();
            Map<String, List<WhiteList>> whiteListMap = whiteList.stream().collect(Collectors.groupingBy(WhiteList::getPeopleId));
            //查询所有设备
            Device device = new Device();
            device.setIsDelete(1);
            List<Device> allDevice = this.deviceMapper.select(device);
            if (allDevice == null || allDevice.size() == 0) {
                return;
            }
            Map<String, Device> deviceMap = new HashMap<>();
            List<String> productCodes = new ArrayList<>();
            for (Device d: allDevice) {
                productCodes.add(d.getProductCode());
                deviceMap.put(d.getProductCode(), d);
            }
            Map<String, Object> parm = new HashMap<>();
            parm.put("whiteList", whiteList);
            parm.put("productCodes", productCodes);
            String json = JSON.toJSONString(parm);
            //请求云端下发接口
            String faceResultString = this.baseHttpUtil.httpPost(json, this.propertiesUtil.getServerHttp() + HttpConstant.FACE_LIST);
            String faceCode = null;
            JSONObject faceJson = null;
            if (null != faceResultString) {
                faceJson = JSONObject.parseObject(faceResultString);
                faceCode = faceJson.get("code").toString();
            }
            if (null != faceJson && "00000000".equals(faceCode)) {
                JSONArray data = faceJson.getJSONArray("data");
                List<Issue> issues = JSONObject.parseArray(data.toJSONString(), Issue.class);
                Map<String, List<Issue>> clusterResultMap = issues.stream().collect(Collectors.groupingBy(Issue::getProductCode));
                for (Map.Entry<String, List<Issue>> entry : clusterResultMap.entrySet()) {
                    String key = entry.getKey();
                    List<Issue> value = entry.getValue();
                    Device d = deviceMap.get(key);
                    if (d == null) {
                        continue;
                    }
                    for (Issue i: value) {
                        Thread.sleep(2000);
                        if (d.getDeviceType() == 3) {
                            //冠林设备
                            List<WhiteList> whiteLists = whiteListMap.get(i.getPeopleId());
                            if (i.getTaskType() == -1){
                                for (WhiteList wl: whiteLists) {
                                    if (wl.getProductCode().equals(i.getProductCode())) {
                                        //删除住户
                                        i.setDevicePeopleId(wl.getDevicePeopleId());
                                        this.myWebSocketLL.tenementDelete(i);
                                        break;
                                    }
                                }
                            } else {
                                i.setDeviceId(d.getDeviceId());
                                if (whiteLists != null) {
                                    for (WhiteList wl: whiteLists) {
                                        if (wl.getProductCode().equals(i.getProductCode())) {
                                            i.setDevicePeopleId(wl.getDevicePeopleId());
                                            this.myWebSocketLL.tenementDelete(i);
                                            break;
                                        }
                                    }
                                }
                                //新增住户
                                this.myWebSocketLL.tenementAdd(i);
                            }
                        }
                    }
                }
            }

            Thread.sleep(5000);
            Issue issue = new Issue();
            issue.setReturnResult(0);
            List<Issue> list = this.issueMapper.select(issue);
            if (list.size() > 0) {
                Map<String, Object> parmIssue = new HashMap<>();
                parmIssue.put("statusList", list);
                String jsonIssue = JSON.toJSONString(parmIssue);
                //请求云端下发同步接口
                String httpPost = this.baseHttpUtil.httpPost(jsonIssue, propertiesUtil.getServerHttp() + HttpConstant.FACE_LIST_RESULT);
                String code = null;
                JSONObject resultJson = null;
                if (null != httpPost) {
                    resultJson = JSONObject.parseObject(httpPost);
                    code = resultJson.get("code").toString();
                }
                if ("00000000".equals(code)) {
                    List<Integer> ids = new ArrayList<>();
                    for (Issue i: list) {
                        ids.add(i.getId());
                    }
                    //修改本地同步状态
                    Example example = new Example(Issue.class);
                    example.createCriteria().andIn("id", ids);
                    issue.setReturnResult(1);
                    this.issueMapper.updateByExampleSelective(issue, example);
                }
            }
        } catch (Exception e) {
            this.logger.error("定时任务GetIssueDataJob异常：" + e.toString(), e);
        }

    }
}

package com.ss.sdk.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.mapper.*;
import com.ss.sdk.model.*;
import com.ss.sdk.socket.MyWebSocketLL;
import com.ss.sdk.utils.BaseHttpUtil;
import com.ss.sdk.utils.HttpConstant;
import com.ss.sdk.utils.PropertiesUtil;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class GetIssueVisitorJob {

    private Logger logger = LoggerFactory.getLogger(GetIssueVisitorJob.class);
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private WhiteListMapper whiteListMapper;
    @Resource
    private IssueVisitorMapper issueVisitorMapper;
    @Resource
    private BaseHttpUtil baseHttpUtil;
    @Resource
    private PropertiesUtil propertiesUtil;
    @Resource
    private MyWebSocketLL myWebSocketLL;

    @Resource
    private WhiteVisitorListMapper whiteVisitorListMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {

        try {
            Long nowTime = System.currentTimeMillis();
            this.logger.info("定时任务GetIssueVisitorJob已经启动" + new Date().toString());
            Example whiteVisitorExam = new Example(WhiteVisitorList.class);
//            whiteVisitorExam.createCriteria().andGreaterThanOrEqualTo("leaveTime", nowTime).andLessThanOrEqualTo("visitorTime", nowTime);
            //查询白名单
            List<WhiteVisitorList> whiteList = this.whiteVisitorListMapper.selectByExample(whiteVisitorExam);
            Map<String, List<WhiteVisitorList>> whiteListMap = whiteList.stream().collect(Collectors.groupingBy(WhiteVisitorList::getPeopleId));
            //查询所有设备
            Device device = new Device();
            device.setIsDelete(1);
            List<Device> allDevice = this.deviceMapper.select(device);
            if (CollectionUtils.isEmpty(allDevice)) {
                return;
            }
            Map<String, List<Device>> deviceMap = allDevice.stream().collect(Collectors.groupingBy(Device::getProductCode));
            List<String> productCodes = allDevice.stream().map(Device::getProductCode).distinct().collect(Collectors.toList());
            //请求体
            Map<String, Object> parm = new HashMap<>();
            parm.put("childrenList", whiteList);
            parm.put("productCodes", productCodes);
            String json = JSON.toJSONString(parm);
            //请求云端下发接口
            String faceResultString = this.baseHttpUtil.httpPost(json, this.propertiesUtil.getServerHttp() + HttpConstant.VISITOR_LIST);
            String faceCode = null;
            JSONObject faceJson = null;
            if (null != faceResultString) {
                faceJson = JSONObject.parseObject(faceResultString);
                faceCode = faceJson.get("code").toString();
            }
            if (null != faceJson && "00000000".equals(faceCode)) {
                JSONArray data = faceJson.getJSONObject("data").getJSONArray("childenList");
                List<IssueVisitor> issues = JSONObject.parseArray(data.toJSONString(), IssueVisitor.class);
                issues.stream().collect(Collectors.groupingBy(IssueVisitor::getProductCode)).forEach((key, value) -> {

                    Device d = deviceMap.get(key).get(0);
                    if (d == null) {
                        return ;
                    }
                    for (IssueVisitor i: value) {
                        if (d.getDeviceType() == 3) {
                            //冠林设备
                            List<WhiteVisitorList> whiteVisitorList = whiteListMap.get(i.getPeopleId());
                            if (i.getTaskType() == -1){
                                i.setDeviceId(d.getDeviceId().substring(0, 4) + "0001");
                                WhiteVisitorList testVisi = whiteVisitorList.stream().filter(k -> k.getProductCode().equals(i.getProductCode())).findFirst().orElse(null);
                                if (testVisi != null){
                                    //删除访客
                                    i.setDevicePeopleId(testVisi.getDevicePeopleId());
                                    this.myWebSocketLL.visitorSignOut(i);
                                }
                            } else {
                                //如果之前有这个人，要先把他签出，然后在去新增
                                whiteVisitorList = CollectionUtils.isEmpty(whiteVisitorList) ? Lists.newArrayList() : whiteVisitorList;
                                WhiteVisitorList testVisi = whiteVisitorList.stream().filter(k -> k.getProductCode().equals(i.getProductCode())).findFirst().orElse(null);
                                i.setDeviceId(d.getDeviceId().substring(0, 4) + "0001");
                                if (testVisi != null){
                                    i.setDevicePeopleId(testVisi.getDevicePeopleId());
                                    this.myWebSocketLL.visitorSignOut(i);
                                }
                                //新增访客
                                this.myWebSocketLL.visitorAdd(i);
                            }
                        }
                    }
                });
            }

            Thread.sleep(5000);
            IssueVisitor issueVisitor = new IssueVisitor();
            issueVisitor.setReturnResult(0);
            List<IssueVisitor> list = this.issueVisitorMapper.select(issueVisitor);
            if (list.size() > 0) {
                Map<String, Object> parmIssue = new HashMap<>();
                parmIssue.put("childenList", list);
                String jsonIssue = JSON.toJSONString(parmIssue);
                //请求云端下发同步接口
                String httpPost = this.baseHttpUtil.httpPost(jsonIssue, propertiesUtil.getServerHttp() + HttpConstant.VISITOR_RESULT);
                String code = null;
                JSONObject resultJson = null;
                if (null != httpPost) {
                    resultJson = JSONObject.parseObject(httpPost);
                    code = resultJson.get("code").toString();
                }
                if ("00000000".equals(code)) {
                    List<Integer> ids = list.stream().map(IssueVisitor::getId).collect(Collectors.toList());
                    //修改本地同步状态
                    Example example = new Example(IssueVisitor.class);
                    example.createCriteria().andIn("id", ids);
                    issueVisitor.setReturnResult(1);
                    this.issueVisitorMapper.updateByExampleSelective(issueVisitor, example);
                    //过时的定时任务全部删除
//                    Example delWhiteExam = new Example(WhiteVisitorList.class);
//                    delWhiteExam.createCriteria().andLessThan("leaveTime", nowTime);
//                    //查询要删除的
//                    List<WhiteVisitorList> delList = this.whiteVisitorListMapper.selectByExample(delWhiteExam);
//                    if (!CollectionUtils.isEmpty(delList)){
//                        Example delExam = new Example(WhiteVisitorList.class);
//                        delExam.createCriteria().andIn("id", delList.stream().map(WhiteVisitorList::getId).collect(Collectors.toList()));
//                        whiteVisitorListMapper.deleteByExample(delExam);
//                    }
                }
            }
        } catch (Exception e) {
            this.logger.error("定时任务GetIssueDataJob异常：" + e.toString(), e);
        }
//        IssueVisitor issueVisitor = new IssueVisitor();
////        issueVisitor.setPeopleName("lyx");
////        issueVisitor.setPeopleId("12");
////        issueVisitor.setDeviceId("010100000".substring(0, 4) + "0001");
////        issueVisitor.setVisitorTime(1646628165000L);
////        issueVisitor.setLeaveTime(1646635845000L);
////        issueVisitor.setPeopleFacePath("http://zkrh-dc.oss-cn-qingdao.aliyuncs.com/people/1646619903705_24f7f9cd4dc44b2faf237e29ccbf9ec6.jpg");
//        issueVisitor.setDevicePeopleId("12");
//        issueVisitor.setProductCode("20201217");
//        issueVisitor.setPeopleId("5");
//        this.myWebSocketLL.visitorSignOut(issueVisitor);
    }
}

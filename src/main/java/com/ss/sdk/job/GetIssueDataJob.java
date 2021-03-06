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
import com.ss.sdk.pojo.terminal.person.PersonInfoList;
import com.ss.sdk.pojo.terminal.request.ImageLists;
import com.ss.sdk.pojo.terminal.request.PersonInfoLists;
import com.ss.sdk.socket.MyWebSocketLL;
import com.ss.sdk.utils.*;
import com.sun.jna.NativeLong;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

        this.logger.info("定时任务GetIssueDataJob已经启动" + new Date().toString());
        //查询白名单
        List<WhiteList> whiteList = this.deviceMapper.findWhiteList();
        Map<String, Object> parm = new HashMap<>();
        parm.put("whiteList", whiteList);
        String json = JSON.toJSONString(parm);
        //请求云端下发接口
        String faceResultString = this.baseHttpUtil.httpPost(json, this.propertiesUtil.getCplatHttp() + HttpConstant.FACE_LIST);
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
                Device d = new Device();
                d.setProductCode(issue.getProductCode());
                Device device = this.deviceMapper.findDevice(d);
                if(device == null){
                    continue;
                }
                if (device.getDeviceType() == 1 && device.getDeviceTypeDetail() != null && device.getDeviceTypeDetail() == 101) {
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
                       this.myWebSocketLL.tenementQuery(issue);
                    } else {
                        issue.setDeviceId(device.getDeviceId().substring(0, 4) + "0001");
                        //新增住户
                        this.myWebSocketLL.tenementAdd(issue);
                    }
                } else if (device.getDeviceType() == 4) {
                    //冠品下发
                    if(issue.getTaskType() == -1){
                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        NameValuePair pair1 = new BasicNameValuePair("pass", device.getPassword());
                        NameValuePair pair2 = new BasicNameValuePair("id", issue.getPeopleId());
                        param.add(pair1);
                        param.add(pair2);
                        String personResult = this.baseHttpUtil.httpPostUrl("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.GUANPIN_PERSON_DELETE, param);
                        if (this.baseHttpUtil.guanpinIsResult(personResult)){
                            this.deviceMapper.delWhiteList(issue);
                            this.logger.info("冠品设备删除人员成功");
                        }
                    } else {
                        List<NameValuePair> personList = new ArrayList<NameValuePair>();
                        NameValuePair pair1 = new BasicNameValuePair("pass", device.getPassword());
                        NameValuePair pair2 = new BasicNameValuePair("person", "{\"id\":\"" + issue.getPeopleId() + "\",\"idcardNum\":\"" + issue.getPeopleId() + "\",\"name\":\" \",\"departmentId\":0}");
                        personList.add(pair1);
                        personList.add(pair2);
                        String personResult = this.baseHttpUtil.httpPostUrl("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.GUANPIN_PERSON_CREATE, personList);
                        if (this.baseHttpUtil.guanpinIsResult(personResult)){
                            //下发人脸
                            List<NameValuePair> faceList = new ArrayList<NameValuePair>();
                            NameValuePair pair3 = new BasicNameValuePair("pass", device.getPassword());
                            NameValuePair pair4 = new BasicNameValuePair("personId", issue.getPeopleId());
                            NameValuePair pair5 = new BasicNameValuePair("imgBase64", Base64Util.imagebase64(issue.getPeopleFacePath()));
                            logger.info("base64:" + issue.getPeopleFacePath());
                            faceList.add(pair3);
                            faceList.add(pair4);
                            faceList.add(pair5);
                            String faceResult = this.baseHttpUtil.httpPostUrl("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.GUANPIN_FACE_CREATE, faceList);
                            if (this.baseHttpUtil.guanpinIsResult(faceResult)){
                                List<NameValuePair> permission = new ArrayList<NameValuePair>();
                                NameValuePair pair6 = new BasicNameValuePair("pass", device.getPassword());
                                NameValuePair pair7 = new BasicNameValuePair("personId", issue.getPeopleId());
                                NameValuePair pair8 = new BasicNameValuePair("time", "2028-01-01 12:00:00");
                                permission.add(pair6);
                                permission.add(pair7);
                                permission.add(pair8);
                                String permissionResult = this.baseHttpUtil.httpPostUrl("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.GUANPIN_PERSON_PERMISSION_CREATE, permission);
                                if (this.baseHttpUtil.guanpinIsResult(permissionResult)){
                                    this.logger.info("冠品设备添加人员权限成功:" + permissionResult);
                                }
                                List<NameValuePair> passTime = new ArrayList<NameValuePair>();
                                NameValuePair pair9 = new BasicNameValuePair("pass", device.getPassword());
                                NameValuePair pair10 = new BasicNameValuePair("passtime", "{\"personId\":\"" + issue.getPeopleId() + "\",\"passtime\":\"00:00:00,23:59:59\"}");
                                passTime.add(pair9);
                                passTime.add(pair10);
                                String passTimeResult = this.baseHttpUtil.httpPostUrl("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.GUANPIN_PERSON_CREATE_PASSTIME, passTime);
                                if (this.baseHttpUtil.guanpinIsResult(passTimeResult)){
                                    this.logger.info("冠品设备添加时间权限成功:" + permissionResult);
                                }
                                issue.setIssueStatus(1);
                                issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
                                this.deviceMapper.insertIssue(issue);
                                this.deviceMapper.insertWhiteList(issue);
                                this.logger.info("冠品设备下发人员成功:" + faceResult);
                            } else {
                                issue.setIssueStatus(2);
                                issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
                                issue.setErrorMessage("人脸接测失败");
                                this.deviceMapper.insertIssue(issue);
                                this.logger.info("冠品设备下发人员失败:" + faceResult);
                            }
                        } else {
                            issue.setIssueStatus(2);
                            issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
                            issue.setErrorMessage("创建人员失败");
                            this.deviceMapper.insertIssue(issue);
                        }
                    }
                }
                else if (device.getDeviceType() == 6) {
                    if (issue.getTaskType() == -1) {
                        //删除
                        Map<String, Object> map = new HashMap();
                        map.put("LastChange", System.currentTimeMillis() / 1000);
                        String jsonStr = JSON.toJSONString(map);
                        String res = this.baseHttpUtil.doDelete("http://" + device.getIp() + ":" + device.getPort() + HttpConstant.YUSHI_DELETE_PEOPLE + issue.getPeopleId(), null, jsonStr);
                        JSONObject resul = null;
                        String resultStr = null;
                        if(null != res){
                            resul = JSONObject.parseObject(res);
                            JSONObject r = (JSONObject) resul.get("Response");
                            resultStr = r.get("ResponseString").toString();
                            if("Succeed".equals(resultStr)){
                                this.deviceMapper.delWhiteList(issue);
                                this.logger.info("宇视设备删除人员成功");
                            }
                        }
                    } else {
                        //宇视下发
                        if (StringUtils.isBlank(issue.getPeopleFacePath())) {
                            this.logger.error("人员无照片");
                            continue;
                        }
                        Map<String, Object> para = new HashMap<>();
                        para.put("Num", 1);
                        List<PersonInfoLists> personList = new ArrayList<>();
                        PersonInfoLists PersonInfoList = new PersonInfoLists();
                        PersonInfoList.setPersonID(Integer.valueOf(issue.getPeopleId()));
                        PersonInfoList.setLastChange(System.currentTimeMillis());
                        PersonInfoList.setPersonName(issue.getPeopleName());
                        PersonInfoList.setTimeTemplateNum(0);
                        PersonInfoList.setIdentificationNum(0);
                        PersonInfoList.setImageNum(1);
                        List<ImageLists> imagList = new ArrayList();
                        ImageLists ImageList = new ImageLists();
                        ImageList.setFaceID(1);
                        ImageList.setName(issue.getPeopleFacePath());
                        String image = Base64Util.imagebase64(issue.getPeopleFacePath()).replaceAll("(\\\r\\\n|\\\r|\\\n|\\\n\\\r)", "");
                        ImageList.setData(image);
                        ImageList.setSize(String.valueOf(Base64Util.getLength(image)));
                        imagList.add(ImageList);
                        PersonInfoList.setImageList(imagList);
                        personList.add(PersonInfoList);
                        para.put("PersonInfoList", personList);
                        String request = JSON.toJSONString(para);
                        String result = this.baseHttpUtil.httpPost(request, "http://" + device.getIp() + ":" + device.getPort() + HttpConstant.YUSHI_INSERT_PEOPLE);
                        this.logger.info("设备添加照片结束！");
                        JSONObject resultJson = null;
                        String resultStr = null;
                        if (null != result) {
                            //获取返回信息
                            resultJson = JSONObject.parseObject(result);
                            JSONObject re = (JSONObject) resultJson.get("Response");
                            resultStr = re.get("ResponseString").toString();
                            //获取返回码
                            JSONObject da = (JSONObject)re.get("Data");
                            JSONArray arr1 = (JSONArray)da.get("PersonList");
                            JSONObject personListObj = (JSONObject)arr1.get(0);
                            JSONArray arr2 =(JSONArray)personListObj.get("FaceList");
                            JSONObject faceListObj = (JSONObject)arr2.get(0);
                            String resuCode = faceListObj.get("ResultCode").toString();
                            if ("Succeed".equals(resultStr) && "0".equals(resuCode)) {
                                issue.setIssueStatus(1);
                                issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
                                //添加白名单
                                this.deviceMapper.insertWhiteList(issue);
                                //添加下发信息表
                                this.deviceMapper.insertIssue(issue);
                                this.logger.info("设备下发成功");
                            } else {
                                issue.setIssueStatus(2);
                                issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
                                issue.setErrorMessage("照片添加设备失败");
                                this.logger.info("设备下发失败：" + resuCode);
                                this.deviceMapper.insertIssue(issue);
                            }
                        }
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

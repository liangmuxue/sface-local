package com.ss.sdk.socket;

import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.mapper.*;
import com.ss.sdk.model.*;
import com.ss.sdk.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* MyWebSocketClientLL
* @author chao
* @create 2020/1/16
* @email lishuangchao@ss-cas.com
**/
public class MyWebSocketClientLL extends WebSocketClient {

    private static final Logger logger = LogManager.getLogger(MyWebSocketClientLL.class);

    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    private MyWebSocketLL myWebSocketLL = ApplicationContextProvider.getBean(MyWebSocketLL.class);
    private WhiteListMapper whiteListMapper = ApplicationContextProvider.getBean(WhiteListMapper.class);
    private IssueMapper issueMapper = ApplicationContextProvider.getBean(IssueMapper.class);
    private IssueVisitorMapper issueVisitorMapper = ApplicationContextProvider.getBean(IssueVisitorMapper.class);
    private CaptureMapper captureMapper = ApplicationContextProvider.getBean(CaptureMapper.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private WhiteVisitorListMapper whiteVisitorListMapper = ApplicationContextProvider.getBean(WhiteVisitorListMapper.class);

    private String uri;
    private Issue issue;
    private IssueVisitor issueVisitor;

    public static String code;
    public static String timestamp;
    public static String sign;

    public MyWebSocketClientLL(String serverUri) throws Exception {
        super(new URI(serverUri));
        this.uri = serverUri;
    }

    public MyWebSocketClientLL(String serverUri, Issue issue) throws Exception {
        super(new URI(serverUri));
        this.uri = serverUri;
        this.issue = issue;
    }

    public MyWebSocketClientLL(String serverUri, IssueVisitor issueVisitor) throws Exception {
        super(new URI(serverUri));
        this.uri = serverUri;
        this.issueVisitor = issueVisitor;
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        logger.info("成功链接冠林服务器");
    }

    @Override
    public void onMessage(String message) {
        try {
            if (uri.contains(HttpConstant.LL_LOGIN)){
                //登陆回调信息
                String loginKey = AESUtil.getLoginKey(this.propertiesUtil.getUserNameLL(), this.propertiesUtil.getPasswordLL());
                String decrypt = AESUtil.decrypt(message, loginKey, loginKey.substring(0, 16));
                logger.info("检测冠林登陆回调信息：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    JSONObject jsonBody = jsonObject.getJSONObject("Body");
                    code = jsonBody.getString("Code");
                    timestamp = jsonBody.getString("Timestamp");
                    sign = jsonBody.getString("Sign");
                    myWebSocketLL.event();
                }
            } else if (uri.contains(HttpConstant.LL_TENEMENT_ADD)) {
                //新增住户回调信息
                String decrypt = getMessage(message);
                logger.info("检测到冠林新增住户回调信息：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    logger.info("新增住户成功");
                    //新增人脸
                    JSONObject bodyObject = jsonObject.getJSONObject("Body");
                    String id = bodyObject.getString("ID");
                    issue.setDevicePeopleId(id);
                    myWebSocketLL.faceAdd(issue);
                } else {
                    String failReason = jsonObject.getString("Message");
                    logger.info("新增住户失败：" + failReason);
                }
            } else if (uri.contains(HttpConstant.LL_FACE_ADD)) {
                //新增人脸回调信息
                String decrypt = getMessage(message);
                logger.info("检测冠林新增人脸回调信息：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    JSONObject bodyObject = jsonObject.getJSONObject("Body");
                    String id = bodyObject.getString("TeneID");
                    WhiteList whiteList = new WhiteList();
                    whiteList.setPeopleId(issue.getPeopleId());
                    whiteList.setProductCode(issue.getProductCode());
                    whiteList.setDevicePeopleId(id);
                    this.whiteListMapper.insert(whiteList);
                    issue.setIssueStatus(1);
                    issue.setReturnResult(0);
                    issue.setIssueTime(System.currentTimeMillis());
                    this.issueMapper.insert(issue);
                    logger.info("新增人脸照片成功");
                } else {
                    String failReason = jsonObject.getString("Message");
                    this.issue.setIssueStatus(2);
                    issue.setReturnResult(0);
                    this.issue.setIssueTime(System.currentTimeMillis());
                    this.issue.setFailReason(failReason);
                    this.issueMapper.insert(issue);
                    logger.info("新增人脸照片失败：" + failReason);
                }
            } else if (uri.contains(HttpConstant.VISTOR_MANA)) {
                //新增访客回调信息
                String decrypt = getMessage(message);
                logger.info("检测冠林新增访客回调信息：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    JSONObject bodyObject = jsonObject.getJSONObject("Body");
                    String id = bodyObject.getString("VIPK");
                    WhiteVisitorList whiteVisitorList = new WhiteVisitorList();
                    whiteVisitorList.setPeopleId(issueVisitor.getPeopleId());
                    whiteVisitorList.setProductCode(issueVisitor.getProductCode());
                    whiteVisitorList.setDevicePeopleId(id);
                    whiteVisitorList.setVisitorTime(issueVisitor.getVisitorTime());
                    whiteVisitorList.setLeaveTime(issueVisitor.getLeaveTime());
                    this.whiteVisitorListMapper.insert(whiteVisitorList);
                    issueVisitor.setIssueStatus(1);
                    issueVisitor.setReturnResult(0);
                    issueVisitor.setIssueTime(System.currentTimeMillis());
                    this.issueVisitorMapper.insert(issueVisitor);
                    logger.info("新增访客成功");
                } else {
                    String failReason = jsonObject.getString("Message");
                    this.issueVisitor.setIssueStatus(2);
                    issueVisitor.setReturnResult(0);
                    this.issueVisitor.setIssueTime(System.currentTimeMillis());
                    this.issueVisitor.setFailReason(failReason);
                    this.issueVisitorMapper.insert(issueVisitor);
                    logger.info("新增访客失败：" + failReason);
                }
            } else if (uri.contains(HttpConstant.VISTOR_SIGNOUT)) {
                //新增访客回调信息
                String decrypt = getMessage(message);
                logger.info("检测冠林删除访客回调信息：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    WhiteVisitorList whiteVisitorList = new WhiteVisitorList();
                    whiteVisitorList.setProductCode(issueVisitor.getProductCode());
                    whiteVisitorList.setPeopleId(issueVisitor.getPeopleId());
                    this.whiteVisitorListMapper.delete(whiteVisitorList);
                    logger.info("删除访客成功");
                } else {
                    String failReason = jsonObject.getString("Message");
                    logger.info("删除访客失败：" + failReason);
                }
            } else if (uri.contains(HttpConstant.LL_TENEMENT_DELETE)) {
                //删除住户回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    WhiteList whiteList = new WhiteList();
                    whiteList.setProductCode(issue.getProductCode());
                    whiteList.setPeopleId(issue.getPeopleId());
                    this.whiteListMapper.delete(whiteList);
                    logger.info("删除住户成功");
                } else {
                    String failReason = jsonObject.getString("Message");
                    logger.info("删除住户失败：" + failReason);
                }
            } else if (uri.contains(HttpConstant.LL_FACE_REMOVE)) {
                //删除人脸回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    WhiteList whiteList = new WhiteList();
                    whiteList.setProductCode(issue.getProductCode());
                    whiteList.setPeopleId(issue.getPeopleId());
                    this.whiteListMapper.delete(whiteList);
                    logger.info("删除人脸成功");
                    myWebSocketLL.faceAdd(this.issue);
                } else {
                    String failReason = jsonObject.getString("Message");
                    logger.info("删除人脸失败：" + failReason);
                }
            } else if (uri.contains(HttpConstant.LL_EVENT)) {
                //事件信息
                String decrypt = getMessage(message);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("1".equals(jsonObject.getString("EventCode")) && "OpenTheDoor".equals(jsonObject.getString("EventType"))){
                    logger.info("检测到门禁通行事件请求");
                    JSONObject eventObject = jsonObject.getJSONObject("EventData");
                    String deviceId = eventObject.getString("DeviceId");
                    String openMode = eventObject.getString("OpenMode");
                    String similarity = eventObject.getString("Similarity");
                    String credentialId = eventObject.getString("CredentialID");
                    String imageBase64 = eventObject.getString("ImageBase64");
                    String recordTime = eventObject.getString("RecordTime");
                    String temp = null;
                    if (eventObject.containsKey("Temperature")){
                        temp = eventObject.getString("Temperature");
                    }
                    //刷脸开门事件
                    Capture capture = new Capture();
                    Device device = new Device();
                    device.setDeviceId(deviceId);
                    device.setIsDelete(1);
                    device = this.deviceMapper.selectOne(device);
                    if (device == null) {
                        return;
                    }

                    if("4".equals(openMode)){
                        capture.setPeopleId(credentialId);
                        capture.setProductCode(device.getProductCode());
                        if (credentialId != null && credentialId.startsWith("V")) {
                            capture.setOpendoorMode(6);
                        } else {
                            capture.setOpendoorMode(1);
                        }
                        capture.setResultCode(1);
                        capture.setRecogScore(Float.valueOf(similarity));
                        if (temp != null){
                            capture.setTemp(Double.valueOf(temp));
                            if (capture.getTemp() > 37.5) {
                                capture.setTempState(1);
                            } else {
                                capture.setTempState(0);
                            }
                        }
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        String url = propertiesUtil.getCaptureUrl() + "/" + capture.getProductCode() + "_" + newName + ".jpg";
                        Base64Util.saveImg(imageBase64, url);
                        capture.setCaptureUrl(url);
                        capture.setCompareDate(System.currentTimeMillis());
                        capture.setCreateTime(System.currentTimeMillis());
                        this.captureMapper.insert(capture);
                    }
                }
            } else if (uri.contains(HttpConstant.LL_OPEN_DOOR)) {
                //开门回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    Capture capture = new Capture();
                    capture.setProductCode(issue.getProductCode());
                    capture.setOpendoorMode(2);
                    capture.setResultCode(1);
                    capture.setCompareDate(issue.getIssueTime());
                    capture.setCreateTime(System.currentTimeMillis());
                    this.captureMapper.insert(capture);
                    logger.info("远程开门成功");
                } else {
                    Capture capture = new Capture();
                    capture.setProductCode(issue.getProductCode());
                    capture.setOpendoorMode(2);
                    capture.setResultCode(0);
                    capture.setCompareDate(issue.getIssueTime());
                    capture.setCreateTime(System.currentTimeMillis());
                    this.captureMapper.insert(capture);
                    logger.info("远程开门失败");
                }
            }
        } catch (Exception e) {
            logger.info("服务器请求处理异常：" + e.toString(), e);
        } finally {
            if (!uri.contains(HttpConstant.LL_EVENT)){
                try {
                    this.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onError(Exception arg0) {
        arg0.printStackTrace();
        logger.info("客户端发生错误,即将关闭!");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        logger.info("客户端已关闭!");
        if (uri.contains(HttpConstant.LL_EVENT) || code == null) {
            code = null;
            logger.info("开始尝试重新连接冠林服务器...");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(60 * 1000);
                        reConnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            SysThreadPool.getThread().execute(thread);
        }
    }

    private void reConnect() throws Exception {
        this.myWebSocketLL.login();
    }

    /**
     * aes解密
     * @param message
     * @return
     * @throws Exception
     */
    private String getMessage(String message) throws Exception {
        String otherKey = AESUtil.getOtherKey(this.propertiesUtil.getUserNameLL());
        String decrypt = AESUtil.decrypt(message, otherKey, otherKey.substring(0, 16));
        return decrypt;
    }

}

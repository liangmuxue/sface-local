package com.ss.sdk.socket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.Issue;
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
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);

    private String uri;
    private Issue issue;

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
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    String body = jsonObject.getString("Body");
                    JSONObject jsonBody = JSONObject.parseObject(body);
                    code = jsonBody.getString("Code");
                    timestamp = jsonBody.getString("Timestamp");
                    sign = jsonBody.getString("Sign");
                }
            } else if (uri.contains(HttpConstant.LL_TENEMENT_ADD)) {
                //新增住户回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    logger.info("新增住户成功");
                    String body = jsonObject.getString("Body");
                    JSONObject bodyObject = JSONObject.parseObject(body);
                    String id = bodyObject.getString("ID");
                    myWebSocketLL.faceAdd(id, this.issue);
                }
            } else if (uri.contains(HttpConstant.LL_FACE_ADD)) {
                //新增人脸回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    logger.info("新增人脸照片成功");
                    this.issue.setIssueStatus(1);
                    this.issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
                    this.deviceMapper.insertIssue(this.issue);
                    this.deviceMapper.insertWhiteList(this.issue);
                } else {
                    logger.info("新增人脸照片失败");
                    this.issue.setIssueStatus(2);
                    this.issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
                    this.issue.setErrorMessage("人脸检测失败");
                    this.deviceMapper.insertIssue(this.issue);
                }
            } else if (uri.contains(HttpConstant.LL_TENEMENT_DELETE)) {
                //删除住户回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    logger.info("删除住户成功");
                    this.deviceMapper.delWhiteList(this.issue);
                }
            } else if (uri.contains(HttpConstant.LL_TENEMENT_QUERY)) {
                //查询住户回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    logger.info("查询住户成功");
                    String body = jsonObject.getString("Body");
                    JSONObject bodyObject = JSONObject.parseObject(body);
                    String list = bodyObject.getString("List");
                    JSONArray jsonArray = JSONArray.parseArray(list);
                    if (jsonArray.size() > 0){
                        String string = jsonArray.getString(0);
                        JSONObject stringObject = JSONObject.parseObject(string);
                        String id = stringObject.getString("ID");
                        this.myWebSocketLL.tenementDelete(id, this.issue);
                    }
                }
            } else if (uri.contains(HttpConstant.LL_EVENT)) {
                //事件信息
                String decrypt = getMessage(message);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("1".equals(jsonObject.getString("EventCode")) && "OpenTheDoor".equals(jsonObject.getString("EventType"))){
                    logger.info("检测到门禁通行事件请求");
                    String eventData = jsonObject.getString("EventData");
                    JSONObject eventObject = JSONObject.parseObject(eventData);
                    String deviceId = eventObject.getString("DeviceId");
                    String openMode = eventObject.getString("OpenMode");
                    String similarity = eventObject.getString("Similarity");
                    String credentialId = eventObject.getString("CredentialID");
                    String imageBase64 = eventObject.getString("ImageBase64");
                    if("4".equals(openMode)){
                        //刷脸开门事件
                        Capture capture = new Capture();
                        capture.setPeopleId(credentialId);
                        capture.setDeviceId(deviceId);
                        capture.setOpendoorMode(1);
                        capture.setResultCode(1);
                        capture.setRecogScore(Float.valueOf(similarity));
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        String url = propertiesUtil.getCaptureUrl() + "/" + capture.getDeviceId() + "_" + newName + ".jpg";
                        Base64Util.saveImg(imageBase64, url);
                        capture.setCaptureUrl(url);
                        capture.setCompareDate(String.valueOf(System.currentTimeMillis()));
                        int result = this.deviceMapper.insertCapture(capture);
                        if (result > 0){
                            logger.info("刷脸认证信息录入成功，设备编号：" + capture.getDeviceId());
                        } else {
                            logger.info("刷脸认证信息录入失败，设备编号：" + capture.getDeviceId());
                        }
                    }
                }
            } else if (uri.contains(HttpConstant.LL_OPEN_DOOR)) {
                //查询住户回调信息
                String decrypt = getMessage(message);
                logger.info("检测到服务器请求：" + decrypt);
                JSONObject jsonObject = JSONObject.parseObject(decrypt);
                if ("0".equals(jsonObject.getString("Result"))){
                    Capture capture = new Capture();
                    capture.setDeviceId(this.issue.getDeviceId());
                    capture.setOpendoorMode(2);
                    capture.setResultCode(1);
                    capture.setCompareDate(String.valueOf(System.currentTimeMillis()));
                    int result = this.deviceMapper.insertCapture(capture);
                    if (result > 0){
                        logger.info("远程开门信息录入成功，设备编号：" + capture.getDeviceId());
                    } else {
                        logger.info("远程开门信息录入失败，设备编号：" + capture.getDeviceId());
                    }
                    logger.info("远程开门成功");
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
        if (uri.contains(HttpConstant.LL_EVENT)){
            try {
                Thread.sleep(30000);
                this.myWebSocketLL.event();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

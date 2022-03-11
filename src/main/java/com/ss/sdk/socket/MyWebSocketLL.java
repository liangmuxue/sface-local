package com.ss.sdk.socket;

import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.model.Issue;
import com.ss.sdk.model.IssueVisitor;
import com.ss.sdk.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * WebSocketLL
 * @author 李爽超 chao
 * @create 2019/12/20
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class MyWebSocketLL implements ApplicationRunner {

    private final static Logger logger = LogManager.getLogger(MyWebSocketLL.class);
    @Resource
    private PropertiesUtil propertiesUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.login();
    }

    /**
     * 登陆
     */
    public void login() {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            String password = this.propertiesUtil.getPasswordLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_LOGIN + "?user=" + userName);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'User':'" + userName + "','Verify':'" + DigestUtils.md5DigestAsHex(password.getBytes()) + "'}";
                String loginKey = AESUtil.getLoginKey(userName, password);
                String encrypt = AESUtil.encrypt(text, loginKey, loginKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备登陆异常：" + e.toString(), e);
        }
    }

    /**
     * 订阅事件
     */
    public void event() {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_EVENT + "?user=" + userName + "&code=" + MyWebSocketClientLL.code);
            clientLL.connectBlocking();
            logger.info("冠林设备订阅事件开启");
        } catch (Exception e) {
            logger.info("冠林设备订阅事件异常：" + e.toString(), e);
        }
    }

    /**
     * 新增住户
     * @param issue
     */
    public void tenementAdd(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_TENEMENT_ADD + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'FrameNo':'" + issue.getDeviceId() + "','Name':'" + issue.getPeopleName() + "','Gender':0,'CredentialType':'111','CredentialID':'" + issue.getPeopleId() + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备添加住户异常：" + e.toString(), e);
        }
    }

    /**
     * 新增访客
     * @param issueVisitor
     */
    public void visitorAdd(IssueVisitor issueVisitor) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.VISTOR_MANA + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issueVisitor);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String imageBase64 = Base64Util.imagebase64(issueVisitor.getPeopleFacePath());
//                String text = "{'FrameNo':'" + issue.getDeviceId() + "','Name':'" + issue.getPeopleName() + "','Gender':0,'CredentialType':'111','CredentialID':'" + issue.getPeopleId() + "'}";
//                String text = "{'VIName':'" + issueVisitor.getPeopleName() + "','VISex':0,'VICredentialType':'111', 'VICredentialID':'" + issueVisitor.getPeopleId() + "', 'VIDestFrameNo':'"+ issueVisitor.getDeviceId() +"', 'VIVisitorTime':"+ issueVisitor.getVisitorTime() +", 'VIImageInBase64':'data:image/jpeg;base64,"+ imageBase64 +"', 'VILeaveTime':"+ issueVisitor.getLeaveTime() +"}";
                String text = "{'VIName':'" + issueVisitor.getPeopleName() + "','VISex':0,'VICredentialType':'111', 'VICredentialID':'" + String.format("%s%s", "V", issueVisitor.getPeopleId()) + "', 'VIPersonNumber': 1, 'VIDestFrameNo':'"+ issueVisitor.getDeviceId() +"', 'VIVisitorTime':'"+ DateUtils.stampToTime(issueVisitor.getVisitorTime(), "yyyy-MM-dd HH:ss:mm") +"', 'VIImageInBase64':'data:image/jpeg;base64,"+ imageBase64 +"', 'VILeaveTime':'"+ DateUtils.stampToTime(issueVisitor.getLeaveTime(), "yyyy-MM-dd HH:ss:mm") +"'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备添加住户异常：" + e.toString(), e);
        }
    }

    /**
     * 访客迁出
     * @param issueVisitor
     */
    public void visitorSignOut(IssueVisitor issueVisitor) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.VISTOR_SIGNOUT + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issueVisitor);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'VIPK':" + BigDecimal.valueOf(Long.parseLong(issueVisitor.getDevicePeopleId())) + "}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备添加住户异常：" + e.toString(), e);
        }
    }

    /**
     * 删除住户
     * @param issue
     */
    public void tenementDelete(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_TENEMENT_DELETE + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'ID':'" + issue.getDevicePeopleId() + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备删除住户异常：" + e.toString(), e);
        }
    }

    /**
     * 新增人脸图片
     * @param issue
     */
    public void faceAdd(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_FACE_ADD + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String imageBase64 = Base64Util.imagebase64(issue.getPeopleFacePath());
                String text = "{'ID':'" + issue.getDevicePeopleId() + "','Photo':'data:image/jpeg;base64," + imageBase64 + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备添加人脸图片异常：" + e.toString(), e);
        }
    }

    /**
     * 删除人脸图片
     * @param issue
     */
    public void faceRemove(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_FACE_REMOVE + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'ID':'" + issue.getDevicePeopleId() + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备删除人脸图片异常：" + e.toString(), e);
        }
    }

    /**
     * 远程开门
     * @param issue
     */
    public void openDoor(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_OPEN_DOOR + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sf.format(issue.getIssueTime());
                String text = "{'DeviceNo':'" + issue.getDeviceId() + "','RecordTime':'" + time + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备远程开门异常：" + e.toString(), e);
        }
    }

}

package com.ss.sdk.socket;

import com.ss.sdk.model.Issue;
import com.ss.sdk.utils.AESUtil;
import com.ss.sdk.utils.Base64Util;
import com.ss.sdk.utils.HttpConstant;
import com.ss.sdk.utils.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

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
        Thread.sleep(2000);
        event();
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
     * 新增住户
     * @param issue
     */
    public void tenementAdd(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_TENEMENT_ADD + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String imageBase64 = Base64Util.imagebase64(issue.getPeopleFacePath());
                String text = "{'FrameNo':'" + issue.getDeviceId() + "','Name':'" + issue.getPeopleId() + "','Telephone':'','Telephone2':'','Gender':0,'CredentialType':'111','CredentialID':'" + issue.getPeopleId() + "','MemberType':'','NativePlace':'','Nationality':'','Nation':'','DegreeOfEdu':'','MaritalStatus':'','AddressCurrent':'','AddressPermanent':'','EmergencyContact':'','EmergencyContactTel':'','PersonnelStatus':'','PersonelFeature':'','WorkUnit':'','WorkUnitAddress':'','WorkUnitMaster':'','WorkUnitTel':'','RelationshipDesc':'','ThirdID':'','Remark':'','PersonKinds':'','PersonRoomRelation':'','FaceImage':'" + imageBase64 + "'}";
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
     * @param id
     */
    public void tenementDelete(String id, Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_TENEMENT_DELETE + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'ID':'" + id + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备删除住户异常：" + e.toString(), e);
        }
    }

    /**
     * 查询住户
     * @param issue
     */
    public void tenementQuery(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_TENEMENT_QUERY + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'TeneID':null,'FrameNo':'" +  issue.getDeviceId() + "','TName':'','Telephone':'','CredentialID':'" + issue.getPeopleId() + "','PageIndex':1,'CountPrePage':1}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备查询住户异常：" + e.toString(), e);
        }
    }

    /**
     * 新增人脸图片
     * @param id
     * @param issue
     */
    public void faceAdd(String id, Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_FACE_ADD + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String imageBase64 = Base64Util.imagebase64(issue.getPeopleFacePath());
                String text = "{'ID':'" + id + "','DeviceOrFrameNo':'','PhotoDesc':'','Photo':'data:image/jpeg;base64," + imageBase64 + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备添加人脸图片异常：" + e.toString(), e);
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
     * 远程开门
     * @param issue
     */
    public void openDoor(Issue issue) {
        try {
            String userName = this.propertiesUtil.getUserNameLL();
            MyWebSocketClientLL clientLL = new MyWebSocketClientLL(this.propertiesUtil.getWebSocketUrlLL() + HttpConstant.LL_OPEN_DOOR + "?user=" + userName + "&code=" + MyWebSocketClientLL.code, issue);
            clientLL.connectBlocking();
            if (clientLL.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                String text = "{'DeviceNo':'" + issue.getDeviceId() + "'}";
                String otherKey = AESUtil.getOtherKey(userName);
                String encrypt = AESUtil.encrypt(text, otherKey, otherKey.substring(0, 16));
                clientLL.send(encrypt);
            }
        } catch (Exception e) {
            logger.info("冠林设备远程开门异常：" + e.toString(), e);
        }
    }

}

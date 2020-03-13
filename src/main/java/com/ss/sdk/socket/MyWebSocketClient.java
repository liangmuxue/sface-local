package com.ss.sdk.socket;

import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.Client.*;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.Device;
import com.ss.sdk.model.Issue;
import com.ss.sdk.utils.ApplicationContextProvider;
import com.ss.sdk.utils.JedisUtil;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import javax.annotation.Resource;
import java.net.URI;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

public class MyWebSocketClient extends WebSocketClient {

    private static final Logger logger = LogManager.getLogger(MyWebSocketClient.class);

    private JedisUtil jedisUtil = ApplicationContextProvider.getBean(JedisUtil .class);
    private RemoteControl remoteControl = ApplicationContextProvider.getBean(RemoteControl .class);
    private CardCfg cardCfg = ApplicationContextProvider.getBean(CardCfg .class);
    private FaceParamCfg faceParamCfg = ApplicationContextProvider.getBean(FaceParamCfg .class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private Alarm alarm = ApplicationContextProvider.getBean(Alarm.class);
    private Basics basics = ApplicationContextProvider.getBean(Basics.class);
    private MyWebSocketLL myWebSocketLL = ApplicationContextProvider.getBean(MyWebSocketLL.class);

    public MyWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        logger.info("开始建立链接...");
    }

    @Override
    public void onMessage(String message) {
        logger.info("检测到云端服务器请求...message：" + message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.parseObject(message);
                String command = jsonObject.getString("command");
                String deviceId = jsonObject.getString("deviceId");
                String peopleId = jsonObject.getString("peopleId");
                String captureUrl = jsonObject.getString("captureUrl");
                Issue issue = new Issue();
                issue.setProductCode(deviceId);
                Device device = deviceMapper.findDevice(issue);
                if ("opendoor".equals(command) && deviceId != null && !"".equals(deviceId)) {
                    if (device.getDeviceType() == 1){
                        boolean isResult = remoteControl.controlGateWay((NativeLong)jedisUtil.get(deviceId));
                        if (isResult){
                            logger.info("远程开门成功");
                        } else {
                            logger.info("远程开门失败");
                            Capture capture = new Capture();
                            capture.setOpendoorMode(2);
                            capture.setResultCode(0);
                            MyWebSocketClient.this.deviceMapper.insertCapture(capture);
                            int iErr = hCNetSDK.NET_DVR_GetLastError();
                            logger.info("建立长连接失败，错误号：" + iErr);
                            if(iErr == 47){
                                NativeLong userId = MyWebSocketClient.this.basics.login(device.getIp(), device.getPort(), device.getUserName(), device.getPassword());
                                if (userId.intValue() != -1) {
                                    MyWebSocketClient.this.jedisUtil.set(String.valueOf(device.getCplatDeviceId()), userId);
                                }
                                int alarmHandle = MyWebSocketClient.this.alarm.SetupAlarmChan(userId);
                                if (alarmHandle == -1) {
                                    int error = hCNetSDK.NET_DVR_GetLastError();
                                    logger.info("设备" + device.getCplatDeviceId() + "布防失败，错误码：" + error);
                                } else {
                                    logger.info("设备" + device.getCplatDeviceId() + "布防成功");
                                }
                            }
                        }
                    } else if (device.getDeviceType() == 3) {
                        issue.setDeviceId(device.getDeviceId());
                        myWebSocketLL.openDoor(issue);
                    }
                }
                if ("issue".equals(command) && deviceId != null && !"".equals(deviceId) && peopleId != null && !"".equals(peopleId) && captureUrl != null && !"".equals(captureUrl)) { ;
                    if (device.getDeviceType() == 1) {
                        issue.setPeopleId(peopleId);
                        issue.setPeopleFacePath(captureUrl);
                        issue.setTaskType(1);
                        cardCfg.setCardInfo((NativeLong) jedisUtil.get(deviceId), issue);
                        faceParamCfg.jBtnSetFaceCfg((NativeLong) jedisUtil.get(deviceId), issue);
                    } else if (device.getDeviceType() == 3) {
                        issue.setDeviceId(device.getDeviceId().substring(0, 4) + "0001");
                        //新增住户
                        myWebSocketLL.tenementAdd(issue);
                    }
                }
            }}) .start();
    }

    @Override
    public void onError(Exception arg0) {
        arg0.printStackTrace();
        logger.info("客户端发生错误,即将关闭!");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        logger.info("客户端已关闭!");
        logger.info("开始尝试重新连接...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reConnect();
            }
        }).start();
    }

    private void reConnect() {
        try {
            MyWebSocketClient client = new MyWebSocketClient(new URI(propertiesUtil.getWebSocketUrl()), new Draft_6455());
            boolean f = client.connectBlocking();
            logger.info("connectBlocking: "+ f);
            if (client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                logger.info("成功链接云端服务器!");
                client.send("{'type':'register','tenantId':'" + propertiesUtil.getTenantId() + "'}");
                MyWebSocket.client = client;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.ss.sdk.Client;

import com.ss.sdk.job.MyApplicationRunner;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Issue;
import com.ss.sdk.socket.ClientCache;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

/**
 * 卡参数管理
 * @author 李爽超 chao
 * @create 2019/12/10
 * @email lishuangchao@ss-cas.com
 **/
@RestController
@Component
public class CardCfg {

    public Logger logger = LoggerFactory.getLogger(MyApplicationRunner.class);

    @Resource
    private DeviceMapper deviceMapper;

    FRemoteCfgCallBackCardGet fRemoteCfgCallBackCardGet;
    FRemoteCfgCallBackCardSet fRemoteCfgCallBackCardSet;

    /**
     * 设置卡参数
     */
    public void setCardInfo(NativeLong lUserID, Issue issue) {
        int iErr = 0;
        HCNetSDK.NET_DVR_CARD_CFG_COND m_struCardInputParam = new HCNetSDK.NET_DVR_CARD_CFG_COND();
        m_struCardInputParam.read();
        m_struCardInputParam.dwSize = m_struCardInputParam.size();
        m_struCardInputParam.dwCardNum = 1;
        m_struCardInputParam.byCheckCardNo = 1;
        Pointer lpInBuffer = m_struCardInputParam.getPointer();
        m_struCardInputParam.write();
        Pointer pUserData = null;
        fRemoteCfgCallBackCardSet = new FRemoteCfgCallBackCardSet();
        NativeLong lHandle = hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD_CFG_V50, lpInBuffer, m_struCardInputParam.size(), fRemoteCfgCallBackCardSet, pUserData);
        if (lHandle.intValue() < 0) {
            logger.info("建立长连接失败，错误号：" + iErr);
            return;
        }
        logger.info("建立设置卡参数长连接成功!");

            HCNetSDK.NET_DVR_CARD_CFG_V50 struCardInfo = new HCNetSDK.NET_DVR_CARD_CFG_V50(); //卡参数
            struCardInfo.read();
            struCardInfo.dwSize = struCardInfo.size();
            struCardInfo.dwModifyParamType = 0x00000001;
            String number = String.valueOf(issue.getPeopleId());
            for (int i = 0; i < number.length(); i++) {
                struCardInfo.byCardNo[i] = number.getBytes()[i];
            }
            if (issue.getTaskType() == -1){
                struCardInfo.byCardValid = 0;
            } else {
                struCardInfo.byCardValid = 1;
            }
            struCardInfo.byCardType = 1;
            struCardInfo.byLeaderCard = 0;
            struCardInfo.byDoorRight[0] = 1; //门1有权限
            struCardInfo.wCardRightPlan[0].wRightPlan[0] = 1; //门1关联卡参数计划模板1
            struCardInfo.dwCardUserId = issue.getPeopleId();
            struCardInfo.byCardModelType = 0;
        //卡有效期
            struCardInfo.struValid.byEnable = 1;
            struCardInfo.struValid.struBeginTime.wYear = 2018;
            struCardInfo.struValid.struBeginTime.byMonth = 12;
            struCardInfo.struValid.struBeginTime.byDay = 1;
            struCardInfo.struValid.struBeginTime.byHour = 0;
            struCardInfo.struValid.struBeginTime.byMinute = 0;
            struCardInfo.struValid.struBeginTime.bySecond = 0;
            struCardInfo.struValid.struEndTime.wYear = 2020;
            struCardInfo.struValid.struEndTime.byMonth = 12;
            struCardInfo.struValid.struEndTime.byDay = 1;
            struCardInfo.struValid.struEndTime.byHour = 0;
            struCardInfo.struValid.struEndTime.byMinute = 0;
            struCardInfo.struValid.struEndTime.bySecond = 0;
            struCardInfo.dwMaxSwipeTime = 0; //无次数限制
            struCardInfo.dwSwipeTime = 0;
            struCardInfo.byCardPassword = "123456".getBytes();
            struCardInfo.dwEmployeeNo = 1;
            struCardInfo.write();
            Pointer pSendBufSet = struCardInfo.getPointer();
            if (!hCNetSDK.NET_DVR_SendRemoteConfig(lHandle, 0x3, pSendBufSet, struCardInfo.size())) {
                iErr = hCNetSDK.NET_DVR_GetLastError();
                logger.info("ENUM_ACS_SEND_DATA失败，错误号：" + iErr);
                return;
            }
            else {
                if (issue.getTaskType() == -1){
                    this.deviceMapper.delWhiteList(issue);
                }
            }
        if (!hCNetSDK.NET_DVR_StopRemoteConfig(lHandle)) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            logger.info("断开长连接失败，错误号：" + iErr);
            return;
        }
        logger.info("断开长连接成功!");
    }

    @RequestMapping(value = {"/setCard"}, method = {RequestMethod.POST})
    public void setCard() {
        NativeLong lUserID = new NativeLong();
        lUserID.setValue(0);
        int iErr = 0;
        HCNetSDK.NET_DVR_CARD_CFG_COND m_struCardInputParam = new HCNetSDK.NET_DVR_CARD_CFG_COND();
        m_struCardInputParam.read();
        m_struCardInputParam.dwSize = m_struCardInputParam.size();
        m_struCardInputParam.dwCardNum = 1;
        m_struCardInputParam.byCheckCardNo = 1;
        Pointer lpInBuffer = m_struCardInputParam.getPointer();
        m_struCardInputParam.write();
        Pointer pUserData = null;
        fRemoteCfgCallBackCardSet = new FRemoteCfgCallBackCardSet();
        NativeLong lHandle = hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD_CFG_V50, lpInBuffer, m_struCardInputParam.size(), fRemoteCfgCallBackCardSet, pUserData);
        if (lHandle.intValue() < 0) {
            logger.info("建立长连接失败，错误号：" + iErr);
            return;
        }
        logger.info("建立设置卡参数长连接成功!");

        HCNetSDK.NET_DVR_CARD_CFG_V50 struCardInfo = new HCNetSDK.NET_DVR_CARD_CFG_V50(); //卡参数
        struCardInfo.read();
        struCardInfo.dwSize = struCardInfo.size();
        struCardInfo.dwModifyParamType = 0x00000001;
        String number = String.valueOf(546);
        for (int i = 0; i < number.length(); i++) {
            struCardInfo.byCardNo[i] = number.getBytes()[i];
        }
        struCardInfo.byCardValid = 0;
        struCardInfo.byCardType = 1;
        struCardInfo.byLeaderCard = 0;
        struCardInfo.byDoorRight[0] = 1; //门1有权限
        struCardInfo.wCardRightPlan[0].wRightPlan[0] = 1; //门1关联卡参数计划模板1
        struCardInfo.dwCardUserId = 546;
        struCardInfo.byCardModelType = 0;
        //卡有效期
        struCardInfo.struValid.byEnable = 1;
        struCardInfo.struValid.struBeginTime.wYear = 2018;
        struCardInfo.struValid.struBeginTime.byMonth = 12;
        struCardInfo.struValid.struBeginTime.byDay = 1;
        struCardInfo.struValid.struBeginTime.byHour = 0;
        struCardInfo.struValid.struBeginTime.byMinute = 0;
        struCardInfo.struValid.struBeginTime.bySecond = 0;
        struCardInfo.struValid.struEndTime.wYear = 2020;
        struCardInfo.struValid.struEndTime.byMonth = 12;
        struCardInfo.struValid.struEndTime.byDay = 1;
        struCardInfo.struValid.struEndTime.byHour = 0;
        struCardInfo.struValid.struEndTime.byMinute = 0;
        struCardInfo.struValid.struEndTime.bySecond = 0;
        struCardInfo.dwMaxSwipeTime = 0; //无次数限制
        struCardInfo.dwSwipeTime = 0;
        struCardInfo.byCardPassword = "123456".getBytes();
        struCardInfo.dwEmployeeNo = 1;
        struCardInfo.write();
        Pointer pSendBufSet = struCardInfo.getPointer();
        if (!hCNetSDK.NET_DVR_SendRemoteConfig(lHandle, 0x3, pSendBufSet, struCardInfo.size())) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            logger.info("ENUM_ACS_SEND_DATA失败，错误号：" + iErr);
            return;
        }
        else {
        }
        if (!hCNetSDK.NET_DVR_StopRemoteConfig(lHandle)) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            logger.info("断开长连接失败，错误号：" + iErr);
            return;
        }
        logger.info("断开长连接成功!");
    }

    /**
     * 获取卡参数
     */
    public void getCardInfo(NativeLong lUserID) {
        int iErr = 0;
        HCNetSDK.NET_DVR_CARD_CFG_COND m_struCardInputParam = new HCNetSDK.NET_DVR_CARD_CFG_COND();
        m_struCardInputParam.dwSize = m_struCardInputParam.size();
        m_struCardInputParam.dwCardNum = 0xffffffff; //查找全部
        m_struCardInputParam.byCheckCardNo = 1;

        Pointer lpInBuffer = m_struCardInputParam.getPointer();
        fRemoteCfgCallBackCardGet = new FRemoteCfgCallBackCardGet();
        m_struCardInputParam.write();

        HCNetSDK.MY_USER_DATA userData = new HCNetSDK.MY_USER_DATA();
        userData.dwSize = userData.size();
        userData.byteData = "1234567".getBytes();
        Pointer pUserData = userData.getPointer();
        userData.write();

        NativeLong lHandle = hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_GET_CARD_CFG_V50, lpInBuffer, m_struCardInputParam.size(), fRemoteCfgCallBackCardGet, pUserData);
        if (lHandle.intValue() < 0) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            logger.info("建立长连接失败，错误号：" + iErr);
            return;
        }
        logger.info("建立获取卡参数长连接成功!");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        if(!hCNetSDK.NET_DVR_StopRemoteConfig(lHandle))
//        {
//            iErr = hCNetSDK.NET_DVR_GetLastError();
//            logger.info("断开长连接失败，错误号：" + iErr);
//            return;
//        }
//        logger.info("断开长连接成功!");
    }

    /**
     * 卡参数设置回调
     */
    public class FRemoteCfgCallBackCardSet implements HCNetSDK.FRemoteConfigCallback {
        @Override
        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {
            logger.info("长连接回调获取数据,NET_SDK_CALLBACK_TYPE_STATUS:" + dwType);
            switch (dwType) {
                case 0:// NET_SDK_CALLBACK_TYPE_STATUS
                    HCNetSDK.REMOTECONFIGSTATUS_CARD struCardStatus = new HCNetSDK.REMOTECONFIGSTATUS_CARD();
                    struCardStatus.write();
                    Pointer pInfoV30 = struCardStatus.getPointer();
                    pInfoV30.write(0, lpBuffer.getByteArray(0, struCardStatus.size()), 0, struCardStatus.size());
                    struCardStatus.read();

                    int iStatus = 0;
                    for (int i = 0; i < 4; i++) {
                        int ioffset = i * 8;
                        int iByte = struCardStatus.byStatus[i] & 0xff;
                        iStatus = iStatus + (iByte << ioffset);
                    }

                    switch (iStatus) {
                        case 1000:// NET_SDK_CALLBACK_STATUS_SUCCESS
                            logger.info("下发卡参数成功,dwStatus:" + iStatus);
                            break;
                        case 1001:
                            logger.info("正在下发卡参数中,dwStatus:" + iStatus);
                            break;
                        case 1002:
                            int iErrorCode = 0;
                            for (int i = 0; i < 4; i++) {
                                int ioffset = i * 8;
                                int iByte = struCardStatus.byErrorCode[i] & 0xff;
                                iErrorCode = iErrorCode + (iByte << ioffset);
                            }
                            logger.info("下发卡参数失败, dwStatus:" + iStatus + "错误号:" + iErrorCode);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 卡参数获取回调
     */
    public class FRemoteCfgCallBackCardGet implements HCNetSDK.FRemoteConfigCallback {
        @Override
        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {
            HCNetSDK.MY_USER_DATA m_userData = new HCNetSDK.MY_USER_DATA();
            m_userData.write();
            Pointer pUserVData = m_userData.getPointer();
            pUserVData.write(0, pUserData.getByteArray(0, m_userData.size()), 0, m_userData.size());
            m_userData.read();

            logger.info("长连接回调获取数据,NET_SDK_CALLBACK_TYPE_STATUS:" + dwType);
            switch (dwType) {
                case 0: //NET_SDK_CALLBACK_TYPE_STATUS
                    HCNetSDK.REMOTECONFIGSTATUS_CARD struCfgStatus = new HCNetSDK.REMOTECONFIGSTATUS_CARD();
                    struCfgStatus.write();
                    Pointer pCfgStatus = struCfgStatus.getPointer();
                    pCfgStatus.write(0, lpBuffer.getByteArray(0, struCfgStatus.size()), 0, struCfgStatus.size());
                    struCfgStatus.read();

                    int iStatus = 0;
                    for (int i = 0; i < 4; i++) {
                        int ioffset = i * 8;
                        int iByte = struCfgStatus.byStatus[i] & 0xff;
                        iStatus = iStatus + (iByte << ioffset);
                    }

                    switch (iStatus) {
                        case 1000:// NET_SDK_CALLBACK_STATUS_SUCCESS
                            logger.info("查询卡参数成功,dwStatus:" + iStatus);
                            break;
                        case 1001:
                            logger.info("正在查询卡参数中,dwStatus:" + iStatus);
                            break;
                        case 1002:
                            int iErrorCode = 0;
                            for (int i = 0; i < 4; i++) {
                                int ioffset = i * 8;
                                int iByte = struCfgStatus.byErrorCode[i] & 0xff;
                                iErrorCode = iErrorCode + (iByte << ioffset);
                            }
                            logger.info("查询卡参数失败, dwStatus:" + iStatus + "错误号:" + iErrorCode);
                            break;
                    }
                    break;
                case 2: //NET_SDK_CALLBACK_TYPE_DATA
                    HCNetSDK.NET_DVR_CARD_CFG_V50 m_struCardInfo = new HCNetSDK.NET_DVR_CARD_CFG_V50();
                    m_struCardInfo.write();
                    Pointer pInfoV30 = m_struCardInfo.getPointer();
                    pInfoV30.write(0, lpBuffer.getByteArray(0, m_struCardInfo.size()), 0, m_struCardInfo.size());
                    m_struCardInfo.read();

                    String str = new String(m_struCardInfo.byCardNo);
                    try {
                        String srtName = new String(m_struCardInfo.byName, "GBK").trim(); //姓名
                        logger.info("查询到的卡号,getCardNo:" + str + "姓名:" + srtName);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

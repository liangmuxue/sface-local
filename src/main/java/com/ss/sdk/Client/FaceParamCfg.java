package com.ss.sdk.Client;

import com.ss.sdk.job.MyApplicationRunner;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Issue;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

/**
 * 人脸参数管理
 *
 * @author 李爽超 chao
 * @create 2019/12/10
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class FaceParamCfg {

    public Logger logger = LoggerFactory.getLogger(MyApplicationRunner.class);

    FRemoteCfgCallBackFaceGet fRemoteCfgCallBackFaceGet;
    FRemoteCfgCallBackFaceSet fRemoteCfgCallBackFaceSet;

    @Resource
    private DeviceMapper deviceMapper;

    public void jBtnSetFaceCfg(NativeLong lUserID, Issue issue) {
        int iErr = 0;
        //设置人脸参数
        HCNetSDK.NET_DVR_FACE_PARAM_COND m_struFaceSetParam = new HCNetSDK.NET_DVR_FACE_PARAM_COND();
        m_struFaceSetParam.dwSize = m_struFaceSetParam.size();
        String number = String.valueOf(issue.getPeopleId());
        for (int i = 0; i < number.length(); i++) {
            m_struFaceSetParam.byCardNo[i] = number.getBytes()[i];
        }
        m_struFaceSetParam.byEnableCardReader[0] = 1;
        m_struFaceSetParam.dwFaceNum = 1;
        m_struFaceSetParam.byFaceID = 1;
        m_struFaceSetParam.write();
        Pointer lpInBuffer = m_struFaceSetParam.getPointer();
        Pointer pUserData = null;
        fRemoteCfgCallBackFaceSet = new FRemoteCfgCallBackFaceSet();
        NativeLong lHandle = hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_FACE_PARAM_CFG, lpInBuffer, m_struFaceSetParam.size(), fRemoteCfgCallBackFaceSet, pUserData);
        if (lHandle.intValue() < 0) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            logger.info("建立长连接失败，错误号：" + iErr);
            return;
        }
        logger.info("建立设置卡参数长连接成功!");
        HCNetSDK.NET_DVR_FACE_PARAM_CFG struFaceInfo = new HCNetSDK.NET_DVR_FACE_PARAM_CFG(); //人脸参数
        struFaceInfo.read();
        struFaceInfo.dwSize = struFaceInfo.size();
        for (int i = 0; i < number.length(); i++) {
            struFaceInfo.byCardNo[i] = number.getBytes()[i];
        }
        struFaceInfo.byEnableCardReader[0] = 1;
        //struFaceInfo.byEnableCardReader[1] = 1;//需要下发人脸的读卡器，按数组表示，每位数组表示一个读卡器，数组取值：0-不下发该读卡器，1-下发到该读卡器
        struFaceInfo.byFaceID = 1; //人脸ID编号，有效取值范围：1~2
        struFaceInfo.byFaceDataType = 1; //人脸数据类型：0- 模板（默认），1- 图片

        /*****************************************
         * 从本地文件里面读取JPEG图片二进制数据
         *****************************************/
        InputStream picfile = null;
        int picdataLength = 0;
        try {
            BufferedInputStream bis = null;
            ByteArrayOutputStream bos = null;
            URL url = new URL(issue.getPeopleFacePath());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3000);
            http.connect();
            bis = new BufferedInputStream(http.getInputStream());
            bos = new ByteArrayOutputStream();
            byte[] buf = new byte[8096];
            int size;
            while ((size = bis.read(buf)) != -1) {
                bos.write(buf, 0, size);
            }
            http.disconnect();
            bos.toByteArray();
            picfile = new ByteArrayInputStream(bos.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            picdataLength = picfile.available();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (picdataLength < 0) {
            logger.info("input file dataSize < 0");
            return;
        }
        HCNetSDK.BYTE_ARRAY ptrpicByte = new HCNetSDK.BYTE_ARRAY(picdataLength);
        try {
            picfile.read(ptrpicByte.byValue);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        ptrpicByte.write();
        /**************************/
        struFaceInfo.dwFaceLen = picdataLength;
        struFaceInfo.pFaceBuffer = ptrpicByte.getPointer();
        struFaceInfo.write();
        Pointer pSendBufSet = struFaceInfo.getPointer();
        //ENUM_ACS_INTELLIGENT_IDENTITY_DATA = 9,  //智能身份识别终端数据类型，下发人脸图片数据
        if (!hCNetSDK.NET_DVR_SendRemoteConfig(lHandle, 0x9, pSendBufSet, struFaceInfo.size())) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            issue.setIssueStatus(0);
            issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
            this.deviceMapper.insertIssue(issue);
            logger.info("NET_DVR_SendRemoteConfig失败，错误号：" + iErr);
            return;
        } else {
            issue.setIssueStatus(1);
            issue.setIssueTime(String.valueOf(System.currentTimeMillis()));
            this.deviceMapper.insertIssue(issue);
            this.deviceMapper.insertWhiteList(issue);
        }
        if (!hCNetSDK.NET_DVR_StopRemoteConfig(lHandle)) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            logger.info("断开长连接失败，错误号：" + iErr);
            return;
        }
        logger.info("断开长连接成功!");

    }

    public class FRemoteCfgCallBackFaceGet implements HCNetSDK.FRemoteConfigCallback {
        @Override
        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {
            System.out.println("长连接回调获取数据,NET_SDK_CALLBACK_TYPE_STATUS:" + dwType);
            switch (dwType) {
                case 0:// NET_SDK_CALLBACK_TYPE_STATUS
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
                            System.out.println("查询人脸参数成功,dwStatus:" + iStatus);
                            break;
                        case 1001:
                            System.out.println("正在查询人脸参数中,dwStatus:" + iStatus);
                            break;
                        case 1002:
                            int iErrorCode = 0;
                            for (int i = 0; i < 4; i++) {
                                int ioffset = i * 8;
                                int iByte = struCfgStatus.byErrorCode[i] & 0xff;
                                iErrorCode = iErrorCode + (iByte << ioffset);
                            }
                            System.out.println("查询人脸参数失败, dwStatus:" + iStatus + "错误号:" + iErrorCode);
                            break;
                    }
                    break;
                case 2: //NET_SDK_CALLBACK_TYPE_DATA
                    HCNetSDK.NET_DVR_FACE_PARAM_CFG m_struFaceInfo = new HCNetSDK.NET_DVR_FACE_PARAM_CFG();
                    m_struFaceInfo.write();
                    Pointer pInfoV30 = m_struFaceInfo.getPointer();
                    pInfoV30.write(0, lpBuffer.getByteArray(0, m_struFaceInfo.size()), 0, m_struFaceInfo.size());
                    m_struFaceInfo.read();
                    String str = new String(m_struFaceInfo.byCardNo).trim();
                    System.out.println("查询到人脸数据关联的卡号,getCardNo:" + str + ",人脸数据类型:" + m_struFaceInfo.byFaceDataType);
                    if (m_struFaceInfo.dwFaceLen > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            fout = new FileOutputStream(newName + "_Card[" + str + "]_ACSFaceCfg.jpg");
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = m_struFaceInfo.pFaceBuffer.getByteBuffer(offset, m_struFaceInfo.dwFaceLen);
                            byte[] bytes = new byte[m_struFaceInfo.dwFaceLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void jBtnGetFaceCfg(NativeLong lUserID) {
        int iErr = 0;
        HCNetSDK.NET_DVR_FACE_PARAM_COND m_struFaceInputParam = new HCNetSDK.NET_DVR_FACE_PARAM_COND();
        m_struFaceInputParam.dwSize = m_struFaceInputParam.size();
        m_struFaceInputParam.byCardNo = "111011".getBytes(); //人脸关联的卡号
        m_struFaceInputParam.byEnableCardReader[0] = 1;
        m_struFaceInputParam.dwFaceNum = 1;
        m_struFaceInputParam.byFaceID = 1;
        m_struFaceInputParam.write();

        Pointer lpInBuffer = m_struFaceInputParam.getPointer();
        Pointer pUserData = null;
        fRemoteCfgCallBackFaceGet = new FRemoteCfgCallBackFaceGet();

        NativeLong lHandle = hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_GET_FACE_PARAM_CFG, lpInBuffer, m_struFaceInputParam.size(), fRemoteCfgCallBackFaceGet, pUserData);
        if (lHandle.intValue() < 0) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            JOptionPane.showMessageDialog(null, "建立长连接失败，错误号：" + iErr);
            return;
        }
        JOptionPane.showMessageDialog(null, "建立获取卡参数长连接成功!");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!hCNetSDK.NET_DVR_StopRemoteConfig(lHandle)) {
            iErr = hCNetSDK.NET_DVR_GetLastError();
            JOptionPane.showMessageDialog(null, "断开长连接失败，错误号：" + iErr);
            return;
        }
        JOptionPane.showMessageDialog(null, "断开长连接成功!");
    }

    public class FRemoteCfgCallBackFaceSet implements HCNetSDK.FRemoteConfigCallback {
        @Override
        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {
            System.out.println("长连接回调获取数据,NET_SDK_CALLBACK_TYPE_STATUS:" + dwType);
            switch (dwType) {
                case 0:// NET_SDK_CALLBACK_TYPE_STATUS
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
                            System.out.println("下发人脸参数成功,dwStatus:" + iStatus);
                            break;
                        case 1001:
                            System.out.println("正在下发人脸参数中,dwStatus:" + iStatus);
                            break;
                        case 1002:
                            int iErrorCode = 0;
                            for (int i = 0; i < 4; i++) {
                                int ioffset = i * 8;
                                int iByte = struCfgStatus.byErrorCode[i] & 0xff;
                                iErrorCode = iErrorCode + (iByte << ioffset);
                            }
                            System.out.println("下发人脸参数失败, dwStatus:" + iStatus + "错误号:" + iErrorCode);
                            break;
                    }
                    break;
                case 2:// 获取状态数据
                    HCNetSDK.NET_DVR_FACE_PARAM_STATUS m_struFaceStatus = new HCNetSDK.NET_DVR_FACE_PARAM_STATUS();
                    m_struFaceStatus.write();
                    Pointer pStatusInfo = m_struFaceStatus.getPointer();
                    pStatusInfo.write(0, lpBuffer.getByteArray(0, m_struFaceStatus.size()), 0, m_struFaceStatus.size());
                    m_struFaceStatus.read();
                    String str = new String(m_struFaceStatus.byCardNo).trim();
                    System.out.println("下发人脸数据关联的卡号:" + str + ",人脸读卡器状态:" +
                            m_struFaceStatus.byCardReaderRecvStatus[0] + ",错误描述:" + new String(m_struFaceStatus.byErrorMsg).trim());
                default:
                    break;
            }
        }
    }

}

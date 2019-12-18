package com.ss.sdk.Client;

import com.sun.jna.NativeLong;
import org.springframework.stereotype.Component;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

/**
 * 注册注销
 * @author 李爽超 chao
 * @create 2019/12/05
 * @email lishuangchao@ss-cas.com
 **/
@Component
public class Basics {

    static HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();

    /**
     * 注册
     * @return
     */
    public NativeLong login(String m_sDeviceIP, int m_sProt, String m_sUsername, String m_sPassword){
        NativeLong lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP, (short)m_sProt, m_sUsername, m_sPassword, m_strDeviceInfo);
        return lUserID;
    }

    /**
     * 注销
     * @return
     */
    public boolean logout(NativeLong lUserID){
        boolean result = hCNetSDK.NET_DVR_Logout_V30(lUserID);
        return result;
    }
}


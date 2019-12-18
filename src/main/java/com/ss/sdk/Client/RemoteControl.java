package com.ss.sdk.Client;

import com.sun.jna.NativeLong;
import org.springframework.web.bind.annotation.RestController;

import static com.ss.sdk.job.MyApplicationRunner.hCNetSDK;

/**
 * 远程开门
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
@RestController
public class RemoteControl {

    public boolean controlGateWay(NativeLong lUserID){
        HCNetSDK.NET_DVR_CONTROL_GATEWAY controlGateway = new HCNetSDK.NET_DVR_CONTROL_GATEWAY();
        controlGateway.dwSize = controlGateway.size();
        controlGateway.dwGatewayIndex = 1;
        controlGateway.byCommand = 1;
        controlGateway.byLockType = 0;
        controlGateway.wLockID = 0;
        controlGateway.byControlSrc[0] = 1;
        controlGateway.write();
        boolean result = hCNetSDK.NET_DVR_RemoteControl(lUserID, 16009, controlGateway.getPointer(), controlGateway.size());
        return result;
    }
}

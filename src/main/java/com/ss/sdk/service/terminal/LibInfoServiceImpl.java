/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2018/11/14
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.service.terminal;

import com.alibaba.fastjson.JSONObject;

import com.ss.sdk.pojo.terminal.libinfo.AllLibInfo;
import com.ss.sdk.pojo.terminal.libinfo.SingleLibInfo;
import com.ss.sdk.pojo.terminal.respone.LAPIResponse;
import com.ss.sdk.service.request.TerminalHttpCall;
import com.ss.sdk.service.terminal.impl.ILibInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author dW5565
 */
@Service
public class LibInfoServiceImpl implements ILibInfoService {

    @Resource
    TerminalHttpCall terminalHttpCall;

    /*
    * 获取所有库信息
    * */
    @Override
    public AllLibInfo getAllLibInfo(String ip) {
        AllLibInfo allLibInfo = null;
        if (terminalHttpCall.get(ip, AllLibInfo.class)) {
            try {
                LAPIResponse lapiResponse = terminalHttpCall.waitForRespone();
                allLibInfo = JSONObject.parseObject(lapiResponse.getData(),AllLibInfo.class);
            } catch (Exception e) {
                System.out.println("获取结果失败" + e);
            }

        }
        return allLibInfo;
    }

    /*
     * 库创建
     * */
    @Override
    public boolean add(String ip,Integer libCode) {

        AllLibInfo allLibInfo = new AllLibInfo();
        allLibInfo.setNum(1);
        List<SingleLibInfo> list = new ArrayList<>();
        allLibInfo.setLibList(list);
        SingleLibInfo singleLibInfo = new SingleLibInfo();
        list.add(singleLibInfo);
        singleLibInfo.setName("Lib"+libCode);
        singleLibInfo.setiD(libCode);
        singleLibInfo.setType(libCode);
        singleLibInfo.setLastChange(System.currentTimeMillis()/1000);

        if (terminalHttpCall.add(ip,allLibInfo,AllLibInfo.class)){
            try {
                LAPIResponse lapiResponse = terminalHttpCall.waitForRespone();
                if(lapiResponse.getStatusCode() == 0 && lapiResponse.getStatusString().equals("Succeed")){
                    return true;
                }
            } catch (Exception e) {
                System.out.println("获取结果失败" + e);
            }
        }
        return false;

    }


}

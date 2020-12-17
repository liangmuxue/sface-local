/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2019/5/7
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.server;


import com.ss.sdk.server.thread.ServerThread;

/**
 * description
 *
 * @author dW5565
 */
public class ServerRun {
    public void start(int port){
        try {
            Thread runthread = new Thread(new ServerThread(port),"LapiServer");
            runthread.start();
        } catch (Exception e) {
            System.out.println("Fail to create loop for:" + e);
        }
    }
}

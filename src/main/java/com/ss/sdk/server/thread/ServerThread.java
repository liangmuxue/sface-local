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
package com.ss.sdk.server.thread;


import com.ss.sdk.server.init.NettyFactory;

/**
 * description
 *
 * @author dW5565
 */
public class ServerThread implements Runnable {
    private int port;

    public ServerThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        NettyFactory nettyFactory = new NettyFactory();
        nettyFactory.createNetty(port);
    }
}

/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2019/5/9
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.server.init;


import com.ss.sdk.server.constant.Operate;
import com.ss.sdk.server.pojo.ChannelBean;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道管理工厂
 *
 * @author dW5565
 */
public class ChannelFactory {
    private static Map<String, ChannelHandlerContext> channelMap = new ConcurrentHashMap<>();
    private static ArrayBlockingQueue<ChannelBean> channelDealQueue = new ArrayBlockingQueue(300);

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //获取队列响应数据
                        ChannelBean channelBean = channelDealQueue.take();
                        InetSocketAddress insocket = (InetSocketAddress) channelBean.getChannelHandlerContext().channel().remoteAddress();
                        String clientIP = insocket.getAddress().getHostAddress();
                        switch (channelBean.getOperate()) {
                            case ADD:
                                channelMap.put(clientIP, channelBean.getChannelHandlerContext());
                                break;
                            /*case DELETE:
                                channelMap.remove(clientIP);
                                break;*/
                        }
                    } catch (Exception e) {
                        System.out.println("[channelDealQueue] : " + e);
                    }
                }
            }
        }).start();
    }

    /*
    * 往队列中塞入数据
    * */
    public static void addChannel(ChannelHandlerContext channelHandlerContext) throws Exception{
        ChannelBean channelBean = new ChannelBean();
        channelBean.setChannelHandlerContext(channelHandlerContext);
        channelBean.setOperate(Operate.ADD);
        channelDealQueue.put(channelBean);
    }

    /*public static void removeChannel(ChannelHandlerContext channelHandlerContext) throws Exception{
        ChannelBean channelBean = new ChannelBean();
        channelBean.setChannelHandlerContext(channelHandlerContext);
        channelBean.setOperate(Operate.DELETE);
        channelDealQueue.put(channelBean);
    }*/

    /*
    * 通道获取
    * */
    public static ChannelHandlerContext getChannel(String ip) {
        ChannelHandlerContext channelHandlerContext = channelMap.get(ip);
        if (channelHandlerContext.channel().isActive()) {
            return channelHandlerContext;
        }
        return null;
    }

}

/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2019/5/8
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.server.handler;


import com.ss.sdk.server.init.ChannelFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * description
 *
 * @author dW5565
 */
public class ClientStateHandler extends ChannelInboundHandlerAdapter {

    /*
    * 添加channel时
    * */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        ChannelFactory.addChannel(ctx);
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"通道被添加");
    }

    /*
     * 删除channel时
     * */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        //ChannelFactory.removeChannel(ctx);
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"通道被删除");
    }

    /**
     * 服务端监听到客户端活动
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //服务端接收到客户端上线通知
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"在线");
    }

    /**
     * 服务端监听到客户端不活动
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //服务端接收到客户端掉线通知
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"掉线");
    }

}

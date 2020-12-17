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
package com.ss.sdk.server.init;


import com.ss.sdk.server.handler.ClientStateHandler;
import com.ss.sdk.server.handler.HttpClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;


/**
 * description
 *
 * @author dW5565
 */
public class LapiServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new HttpRequestEncoder());

        //保活超时时间
        pipeline.addLast(new ReadTimeoutHandler(600));
        //通道状态打印
        pipeline.addLast(new ClientStateHandler());
        //处理
        pipeline.addLast(new HttpClientHandler());


    }
}

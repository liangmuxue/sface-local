/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.service.black.httpclient
 * Date Created: 2018/7/19
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.service.request;


import com.alibaba.fastjson.JSON;

import com.ss.sdk.pojo.terminal.respone.LAPIResponse;
import com.ss.sdk.server.init.ChannelFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 终端http调用
 *
 * @author dW5565
 */
@Component
public class TerminalHttpCall {

    /**
     * 日志操作
     */
    private static final Map<String, String> map = new HashMap<>();
    private static ArrayBlockingQueue<LAPIResponse> resultQueue = new ArrayBlockingQueue<>(300);


    static {
        map.put("AllLibInfo", "/LAPI/V1.0/PeopleLibraries/BasicInfo");
        map.put("PersonInfoList","/LAPI/V1.0/PeopleLibraries/3/People");
    }

    private <E> String getURI(Class<E> clazz) {
        return map.get(clazz.getSimpleName());
    }

    public static void setResultQueue(LAPIResponse result) throws Exception{
        resultQueue.put(result);
    }

    public <E> LAPIResponse waitForRespone() throws Exception{
        LAPIResponse lapiResponse = TerminalHttpCall.resultQueue.take();
        return lapiResponse;
    }

    public <E> boolean get(String ip,Class<E> clazz) {
        try {
            URI url = new URI(getURI(clazz));
            //配置HttpRequest的请求数据和一些配置信息
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url.toASCIIString());
            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ChannelHandlerContext ctx = ChannelFactory.getChannel(ip);
            if(ctx == null){
                return false;
            }
            ctx.writeAndFlush(request);
        }catch (Exception e){
            System.out.println( e);
            return false;
        }
        return true;
    }

    public <E> boolean add(String ip, E e, Class<E> clazz) {
        try {
            URI url = new URI(getURI(clazz));

            //配置HttpRequest的请求数据和一些配置信息
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST, url.toASCIIString(), Unpooled.wrappedBuffer(JSON.toJSONString(e).getBytes("UTF-8")));

            request.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    //开启长连接
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    //设置传递请求内容的长度
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

            ChannelHandlerContext ctx = ChannelFactory.getChannel(ip);
            if(ctx == null){
                return false;
            }
            ctx.writeAndFlush(request);
            return true;
        }catch (Exception exc){
            System.out.println(exc);
        }
        return false;
    }

}

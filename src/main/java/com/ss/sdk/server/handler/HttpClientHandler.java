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
package com.ss.sdk.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.ss.sdk.pojo.terminal.model.PersonVerification;
import com.ss.sdk.pojo.terminal.respone.LAPIResponse;
import com.ss.sdk.service.data.impl.IPersonDataService;
import com.ss.sdk.service.request.TerminalHttpCall;
import com.ss.sdk.utils.ApplicationContextProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * description
 *
 * @author dW5565
 */
public class HttpClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(HttpClientHandler.class);
    private IPersonDataService personDataService = ApplicationContextProvider.getBean(IPersonDataService.class);
    static String data = "";
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        String content = (String) msg;
        if (content.contains("/LAPI/V1.0/PACS/Controller/HeartReportInfo")) {
            //心跳打印
            logger.info("心跳接收");
        }else if (StringUtils.contains(content, "Response")) {
            logger.info("响应报文 : " + content);
            Map respone = JSONObject.parseObject(content, Map.class);
            LAPIResponse lapiResponse = null;
            try {
                lapiResponse = JSON.parseObject(respone.get("Response").toString(), LAPIResponse.class);
            }catch (Exception e){
                logger.info("fail to parse :"+e);
            }
            //获取响应塞入队列中
            TerminalHttpCall.setResultQueue(lapiResponse);
        } else {
            if (StringUtils.contains(content, "/LAPI/V1.0/PACS/Controller/Event/Notifications")) {
                logger.info("推送接收" + content);
               int postNum = content.indexOf("POST");
               int closeNum = content.indexOf("close");
               if(postNum != 0){
                   data = data + content.substring(0,postNum);
                   PersonVerification personVerification = JSON.parseObject(data, PersonVerification.class);
                   this.personDataService.addPerson(personVerification);
                   data = "";
                   data = content.substring(closeNum +5);
               }else{
                   logger.info("推送接收拼接" + content);
                   data = "";
                   int index = content.indexOf("{");
                   data = data + content.substring(index);
               }
                /*data = "";
                int index = content.indexOf("{");
                data = data + content.substring(index);
                if(isJson(data)){
                    PersonVerification personVerification = JSON.parseObject(data, PersonVerification.class);
                    this.personDataService.addPerson(personVerification);
                }*/
            } else {
                data = data + content;
                /*if(isJson(data)){
                    PersonVerification personVerification = JSON.parseObject(data, PersonVerification.class);
                    this.personDataService.addPerson(personVerification);
                }*/
            }
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"异常 : "+ cause);
        //异常出现就关闭连接
        if(incoming.isActive()){ctx.close();}
    }

    public static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

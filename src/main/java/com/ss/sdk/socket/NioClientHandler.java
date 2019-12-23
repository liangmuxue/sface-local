package com.ss.sdk.socket;

import com.alibaba.fastjson.JSONObject;
import com.ss.sdk.Client.CardCfg;
import com.ss.sdk.Client.FaceParamCfg;
import com.ss.sdk.Client.RemoteControl;
import com.ss.sdk.model.Issue;
import com.ss.sdk.utils.ApplicationContextProvider;
import com.ss.sdk.utils.JedisUtil;
import com.ss.sdk.utils.PropertiesUtil;
import com.sun.jna.NativeLong;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class NioClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LogManager.getLogger(NioClientHandler.class);

    private static NioClientHandler nioClientHandler;

    @Resource
    private JedisUtil jedisUtil;

    @Resource
    private RemoteControl remoteControl;

    @Resource
    private CardCfg cardCfg;

    @Resource
    private FaceParamCfg faceParamCfg;

    public NioClientHandler() { }

    @PostConstruct
    public void init() {
        nioClientHandler = this;
        nioClientHandler.jedisUtil = this.jedisUtil;
    }

    /**
     *客户端接收消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.parseObject(msg.toString());
                String command = jsonObject.getString("command");
                String deviceId = jsonObject.getString("deviceId");
                String peopleId = jsonObject.getString("peopleId");
                String captureUrl = jsonObject.getString("captureUrl");
                if ("opendoor".equals(command) && deviceId != null && !"".equals(deviceId)) {
                    boolean isResult = nioClientHandler.remoteControl.controlGateWay((NativeLong)nioClientHandler.jedisUtil.get(deviceId));
                    if (isResult){
                        logger.info("远程开门成功");
                    } else {
                        logger.info("远程开门失败");
                    }
                }
                if ("issue".equals(command) && deviceId != null && !"".equals(deviceId) && peopleId != null && !"".equals(peopleId) && captureUrl != null && !"".equals(captureUrl)) {
                    Issue issue = new Issue();
                    issue.setPeopleId(Integer.valueOf(peopleId));
                    issue.setPeopleFacePath(captureUrl);
                    issue.setTaskType(1);
                    nioClientHandler.cardCfg.setCardInfo((NativeLong)nioClientHandler.jedisUtil.get(deviceId), issue);
                    nioClientHandler.faceParamCfg.jBtnSetFaceCfg((NativeLong)nioClientHandler.jedisUtil.get(deviceId), issue);
                }
            }}) .start();
    }

    /*
     * 读取完毕 服务端发送过来的数据之后的操作
     */
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        logger.info("服务端接收数据完毕..");
//    }

    /**
     * 连接关闭!
     * */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.error(" {}连接关闭！",ctx.channel().localAddress().toString() );
        SocketChannel channel= (SocketChannel) ctx.pipeline().channel();
        for (String key :ClientCache.clientMap.keySet()) {
            if (ClientCache.clientMap.get(key).getSocketChannel()==channel) {
                ClientCache.clientMap.get(key).restart();
            }
        }
        //  removeChannnelMap(ctx);
        // 关闭流
        ctx.close();
    }

    /**
     * 客户端主动连接服务端
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("RemoteAddress"+ ctx.channel().remoteAddress() + " active !");
        // logger.info("msg"+ ctx.m + " active !");
        //ctx.writeAndFlush("连接成功！");
        super.channelActive(ctx);
        WriteThread firstThread = new WriteThread();
        firstThread.start();
    }

    /**
     * 发生异常处理
     * */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        //  logger.error("连接异常,连接异常："+ DateUtils.dateToString(new Date())+cause.getMessage(), cause);
        ctx.fireExceptionCaught(cause);
        //   removeChannnelMap(ctx);
        ctx.close();
    }

    /**
     *删除map中ChannelHandlerContext
     *  */
//    private void removeChannnelMap(ChannelHandlerContext ctx){
//      for( String key :NioServer.map.keySet()){
//            if( NioServer.map.get(key)!=null &&  NioServer.map.get(key).equals( ctx)){
//                NioServer.map.remove(key);
//            }
//        }
//    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                ctx.close();
            }
            //标志该链接已经close 了 
        }
    }

    class WriteThread extends Thread {

        private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);

        @Override
        public void run() {
            ClientCache.clientMap.get("id").writeAndFlush("id=" + propertiesUtil.getTenantId() + "\r\n");
        }
    }

}
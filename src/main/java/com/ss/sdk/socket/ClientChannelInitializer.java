package com.ss.sdk.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

          /* LineBasedFrameDecoder的工作原理是：依次遍历ByteBuf中的可读字节，
        判断看其是否有”\n” 或 “\r\n”， 如果有就以此位置为结束位置。
        从可读索引到结束位置的区间的字节就组成了一行。 它是以换行符为结束标志的解码器，
        支持携带结束符和不带结束符两种解码方式，同时支持配置单行的最大长度，
        如果读到了最大长度之后仍然没有发现换行符，则抛出异常，同时忽略掉之前读到的异常码流。*/
        pipeline.addLast(new LineBasedFrameDecoder(10010));
        //字符串解码和编码
        //LineBasedFrameDecoder + StringDecoder 就是一个按行切换的文本解码器。
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        //服务器的逻辑
        pipeline.addLast("handler", new NioClientHandler());
    }
}

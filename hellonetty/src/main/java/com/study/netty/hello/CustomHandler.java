package com.study.netty.hello;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 描述: 创建自定义助手类
 *
 * @author li
 * @date 2019/12/20
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //获取当前channel
        Channel channel = ctx.channel();
        if (!(msg instanceof HttpRequest)){
            return;
        }
        //显示客户端远程处理
        System.out.println("Client Remote Address: " + channel.remoteAddress());
        //定义发送的数据消息
        ByteBuf buffer = Unpooled.copiedBuffer("Hello Netty~~ ", CharsetUtil.UTF_8);
        //构建一个http response
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,buffer);
        //为响应增加数据类型和长度
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,buffer.readableBytes());
        //把响应传到客户端
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("channel 异常...");
        System.out.println(cause.getMessage());
        Channel channel = ctx.channel();
        channel.closeFuture();
    }
}

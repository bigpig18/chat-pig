package com.chat.pig.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述: 自定义handler，处理消息
 * TextWebSocketFrame: netty中用于为websocket专门处理文本的对象，frame是消息的载体
 *
 * @author li
 * @date 2019/12/23
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 用于记录和管理所有客户端的channel
     */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端发送来的字符串
        String context = msg.text();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("接收到的信息: " + context);
        for (Channel channel : clients) {
            //不可以添加字符串
            channel.writeAndFlush(new TextWebSocketFrame("服务器接收到消息["+ date +"]: " + context));
        }
        //下面的方法与上述方法结果一致
//        clients.writeAndFlush(new TextWebSocketFrame("服务器接收到消息["+ date +"]: " + context));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Handler Added...");
        //获取channel
        //将channel放入ChannelGroup中进行管理
        clients.add(ctx.channel());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Handler Removed: channelLongId - " + ctx.channel().id().asLongText());
    }
}

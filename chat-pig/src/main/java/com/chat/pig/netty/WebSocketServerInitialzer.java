package com.chat.pig.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 描述: 初始化器
 *
 * @author li
 * @date 2019/12/23
 */
public class WebSocketServerInitialzer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //获取通道
        ChannelPipeline pipeline = channel.pipeline();

        //用于支持http协议
        //websocket 基于 http 协议，所以要有 http 编解码器
        pipeline.addLast(new HttpServerCodec());
        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //http聚合器，聚合成FullHttpRequest 或 FullHttpResponse
        //在netty中的编程，大概率会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //支持websocket
        /*
         * websocket服务器处理的协议，并指定给客户端连接访问的路由: /ws
         * 同时会帮助处理繁重复杂的事情
         * 比如 handshaking(close,ping,pong)
         * 对于websocket来讲，都是以frames进行传输，不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //自定义handler
        pipeline.addLast(new ChatHandler());
    }
}

package com.chat.pig.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述: webSocket服务端
 *
 * @author li
 * @date 2019/12/24
 */
@Slf4j
@Component
public class WebSocketServer {

    private ServerBootstrap server;

    private static class SingletonWebSocketSever {
        static final WebSocketServer INSTANCE = new WebSocketServer();
    }

    public static WebSocketServer getInstance(){
        return SingletonWebSocketSever.INSTANCE;
    }

    public WebSocketServer(){
        //定义线程组
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();
        //创建netty服务器
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketServerInitialzer());
    }

    public void start(){
        ChannelFuture channelFuture = server.bind(8088);
        log.debug("Netty WebSocket Server Starting...");
    }
}

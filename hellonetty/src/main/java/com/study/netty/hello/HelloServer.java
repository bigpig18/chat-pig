package com.study.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 描述: netty 实现Client Server 端实时通信
 *
 * @author li
 * @date 2019/12/20
 */
public class HelloServer {

    public static void main(String[] args) throws InterruptedException {

        //定义一对线程组
        //主线程组，用于接收客户端连接，不做其他任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        System.out.println("BOSS-GROUP");
        //从线程组，主线程组将任务给它，让其做处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("WORKER-GROUP");
        try {
            //netty服务器的创建，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置主从线程组
            serverBootstrap.group(bossGroup,workerGroup)
                    //设置nio双向通道
                    .channel(NioServerSocketChannel.class)
                    //子处理器，用于处理workerGroup
                    .childHandler(new HelloServerInitializer());
            //启动server并设置8088为启动端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
            System.out.println("SERVER START AND BIND PORT: 8088");
            //用于监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
            System.out.println("CHANNEL CLOSE");
        } finally {
            //关闭主从线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

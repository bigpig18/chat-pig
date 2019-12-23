package com.study.netty.hello;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 描述: 初始化器，channel注册后，会执行里面相应的初始化方法
 *
 * @author li
 * @date 2019/12/20
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过channel获取对应的管道
        ChannelPipeline pipeline = channel.pipeline();
        //通过管道添加handler
        //HttpServerCodec是由netty自己提供的助手类(编解码器)
        //当请求到服务端，我们需要做解码，响应到客户端做编码
        pipeline.addLast("HttpServer",new HttpServerCodec());
        //添加自定义handler,返回 "Hello Netty"
        pipeline.addLast("customHandler",new CustomHandler());
    }

}

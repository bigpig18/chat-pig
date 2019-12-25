package com.chat.pig;

import com.chat.pig.netty.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 描述: 启动netty服务器
 *
 * @author li
 * @date 2019/12/24
 */
@Slf4j
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null){
            try {
                WebSocketServer.getInstance().start();
            } catch (Exception e) {
                log.error("Netty WebSocket Server Start Error...{}",e.getMessage());
            }
        }
    }
}
